package com.example.damandroid.presentation.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.damandroid.domain.model.ChatMessage
import com.example.damandroid.domain.repository.ChatRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val error: String? = null,
    val isSending: Boolean = false
)

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val chatId: String
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChatUiState(isLoading = true))
    val uiState: StateFlow<ChatUiState> = _uiState
    
    private var pollingJob: Job? = null
    private val pollingIntervalMs = 3000L // 3 secondes
    
    init {
        loadMessagesWithRetry()
        markChatAsRead()
    }
    
    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
    
    private fun loadMessagesWithRetry(maxRetries: Int = 5, delayMs: Long = 1000) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            var lastError: Throwable? = null
            repeat(maxRetries) { attempt ->
                runCatching {
                    chatRepository.getMessages(chatId)
                }.onSuccess { messages ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            messages = messages,
                            error = null
                        )
                    }
                    // Démarrer le polling après le chargement initial réussi
                    if (pollingJob?.isActive != true) {
                        startPolling()
                    }
                    return@launch // Succès, on sort
                }.onFailure { throwable ->
                    lastError = throwable
                    // Si c'est une erreur 403 et qu'on n'est pas au dernier essai, on retry
                    val is403Error = throwable.message?.contains("403", ignoreCase = true) == true ||
                                    throwable.message?.contains("accès", ignoreCase = true) == true ||
                                    throwable.message?.contains("access", ignoreCase = true) == true
                    
                    if (is403Error && attempt < maxRetries - 1) {
                        // Attendre avant de réessayer avec délai progressif
                        kotlinx.coroutines.delay(delayMs * (attempt + 1)) // 1s, 2s, 3s, 4s, 5s
                    } else {
                        // Dernier essai ou erreur non-403, on affiche l'erreur
                        val errorMessage = if (is403Error && attempt == maxRetries - 1) {
                            // Message d'erreur plus explicite pour 403 après tous les essais
                            "Vous n'avez pas accès à ce chat. Le backend n'a pas encore synchronisé vos permissions. Veuillez réessayer dans quelques instants ou contactez le support si le problème persiste."
                        } else {
                            throwable.message ?: "Erreur lors du chargement des messages"
                        }
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                        return@launch
                    }
                }
            }
            
            // Si on arrive ici, tous les essais ont échoué
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = lastError?.message ?: "Échec du chargement des messages après $maxRetries tentatives"
                )
            }
        }
    }
    
    fun loadMessages() {
        loadMessagesWithRetry()
    }
    
    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, error = null) }
            runCatching {
                chatRepository.sendMessage(chatId, text)
            }.onSuccess { message ->
                _uiState.update { state ->
                    state.copy(
                        isSending = false,
                        messages = state.messages + message
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isSending = false,
                        error = throwable.message ?: "Failed to send message"
                    )
                }
            }
        }
    }
    
    private fun markChatAsRead() {
        viewModelScope.launch {
            runCatching {
                chatRepository.markChatAsRead(chatId)
            }.onFailure {
                // Log error but don't show to user
                android.util.Log.e("ChatViewModel", "Failed to mark chat as read: ${it.message}")
            }
        }
    }
    
    fun refresh() {
        loadMessages()
    }
    
    /**
     * Démarrer le polling pour les nouveaux messages
     */
    private fun startPolling() {
        if (pollingJob?.isActive == true) return
        
        pollingJob = viewModelScope.launch {
            while (true) {
                delay(pollingIntervalMs)
                
                // Charger les messages sans afficher l'état de chargement
                runCatching {
                    val newMessages = chatRepository.getMessages(chatId)
                    val currentMessages = _uiState.value.messages
                    
                    // Ne mettre à jour que si les messages ont changé
                    if (newMessages.size != currentMessages.size || 
                        newMessages.any { newMsg -> 
                            currentMessages.none { it.id == newMsg.id }
                        }) {
                        _uiState.update { state ->
                            state.copy(messages = newMessages)
                        }
                    }
                }.onFailure {
                    // En cas d'erreur, on continue le polling silencieusement
                    // (ne pas spammer l'utilisateur avec des erreurs)
                    android.util.Log.d("ChatViewModel", "Polling error: ${it.message}")
                }
            }
        }
    }
    
    /**
     * Arrêter le polling
     */
    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }
}

