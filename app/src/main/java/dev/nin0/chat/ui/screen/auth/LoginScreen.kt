package dev.nin0.chat.ui.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.nin0.chat.ui.component.LoadingButton
import dev.nin0.chat.ui.screen.auth.viewmodel.LoginViewModel
import dev.nin0.chat.ui.screen.chat.ChatScreen

class LoginScreen: Screen {

    @Composable
    override fun Content() {
        val viewModel: LoginViewModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(viewModel.loginSuccess) {
            if (viewModel.loginSuccess) navigator.replaceAll(ChatScreen())
        }

        Scaffold { pv ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(pv)
            ) {
                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current
                val canSubmit = viewModel.email.isNotBlank() && viewModel.password.isNotBlank()

                OutlinedTextField(
                    label = { Text("Email") },
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() }),
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    label = { Text("Password") },
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (canSubmit && !viewModel.loading) viewModel.login()
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                LoadingButton(
                    onClick = viewModel::login,
                    loading = viewModel.loading,
                    enabled = canSubmit,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
            }
        }
    }

}