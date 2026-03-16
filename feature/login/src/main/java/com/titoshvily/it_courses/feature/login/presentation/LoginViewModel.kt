package com.titoshvily.it_courses.feature.login.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoginEnabled: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                isLoginEnabled = isValidEmail(email) && it.password.isNotBlank()
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                isLoginEnabled = isValidEmail(it.email) && password.isNotBlank()
            )
        }
    }

    private fun isValidEmail(email: String): Boolean {
        if (email.any { it in '\u0400'..'\u04FF' }) return false
        return Regex("""^[\w.%+\-]+@[\w.\-]+\.[a-zA-Z]{2,}$""").matches(email)
    }
}
