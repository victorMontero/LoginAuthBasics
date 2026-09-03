# Configuração do Hilt no Projeto

Este plano detalha as correções e adições necessárias para integrar o Hilt corretamente no seu projeto Android com Compose, corrigindo os erros de configuração nos arquivos Gradle e no código Kotlin.

## User Review Required

> [!NOTE]
> Foram detectados erros de digitação nos grupos das dependências no `libs.versions.toml` (ex: `androidx.hilt` para o core do Hilt e `google.com.dagger`). O plano corrige isso para o grupo oficial `com.google.dagger`.

## Proposed Changes

### [Component Name]

#### [MODIFY] [libs.versions.toml](file:///home/veechie/AndroidStudioProjects/LoginAuthBasics/gradle/libs.versions.toml)
Corrigir os grupos das bibliotecas Hilt e adicionar a biblioteca de integração com Navigation Compose.

#### [MODIFY] [build.gradle.kts (:app)](file:///home/veechie/AndroidStudioProjects/LoginAuthBasics/app/build.gradle.kts)
Adicionar a dependência `hilt-navigation-compose` para permitir o uso de `hiltViewModel()`.

#### [MODIFY] [LoginApplication.kt](file:///home/veechie/AndroidStudioProjects/LoginAuthBasics/app/src/main/java/com/android/loginauthbasics/LoginApplication.kt)
Corrigir a anotação para `@HiltAndroidApp`.

#### [MODIFY] [MainActivity.kt](file:///home/veechie/AndroidStudioProjects/LoginAuthBasics/app/src/main/java/com/android/loginauthbasics/MainActivity.kt)
Adicionar a anotação `@AndroidEntryPoint`.

## Verification Plan

### Automated Tests
- Executar `./gradlew assembleDebug` para garantir que a geração de código do Hilt/KSP está funcionando.

### Manual Verification
- Sincronizar o projeto no Android Studio.
