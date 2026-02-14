# 📱 MyMidiaList

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=google-play&logoColor=white)

> **O seu organizador pessoal de cultura pop.** > Nunca mais esqueça em qual episódio você parou ou aquele mangá que queria ler.

---

## 🖼️ Telas do Projeto

<div align="center">
  <img src="https://via.placeholder.com/200x400?text=Tela+Principal" alt="Tela Principal" height="400">
  <img src="https://via.placeholder.com/200x400?text=Busca+Animes" alt="Busca" height="400">
  <img src="https://via.placeholder.com/200x400?text=Detalhes" alt="Detalhes" height="400">
</div>

---

## 🚀 Sobre o Projeto

O **MyMidiaList** é um aplicativo nativo Android desenvolvido para gerenciar o consumo de mídias de entretenimento. O app permite pesquisar obras, salvar em uma biblioteca pessoal e organizar por status (Lendo, Assistindo, Concluído).

O foco principal é resolver o problema de "listas espalhadas", centralizando Animes, Mangás, Séries e Livros em um único lugar offline e rápido.

## ✨ Funcionalidades

- 🔍 **Busca Inteligente:** Integração com a **Jikan API (MyAnimeList)** para buscar Animes e Mangás.
- 🏷️ **Filtros Dinâmicos:** Alterne facilmente entre busca de TV (Animes) e Livros (Mangás/Novels).
- 💾 **Banco de Dados Local:** Seus dados salvos funcionam 100% offline usando **Room Database**.
- 📂 **Organização Automática:**
    - Animes salvos vão automaticamente para a aba "Séries/Filmes".
    - Mangás salvos vão para a aba "Livros".
- 🎨 **UI Moderna:** Interface construída 100% em **Jetpack Compose** com Material Design 3.

---

## 🛠️ Tech Stack (Tecnologias)

O projeto segue a arquitetura **MVVM (Model-View-ViewModel)** e utiliza as bibliotecas mais modernas do desenvolvimento Android:

* **Linguagem:** [Kotlin](https://kotlinlang.org/)
* **Interface (UI):** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material3)
* **Navegação:** Activities & Intents
* **Consumo de API:** [Retrofit 2](https://square.github.io/retrofit/) + GSON
* **Carregamento de Imagens:** [Coil](https://coil-kt.github.io/coil/)
* **Banco de Dados:** [Room Database](https://developer.android.com/training/data-storage/room)
* **Assincronismo:** Coroutines & Flow
* **Injeção de Dependência:** Manual (Repository Pattern)

---

## 🔌 API Utilizada

Este projeto utiliza a **Jikan API** (Unofficial MyAnimeList API).
* Documentação: [https://jikan.moe/](https://jikan.moe/)
* *Nota: O app filtra buscas por popularidade e conteúdo seguro (SFW).*

---

## ⚙️ Como rodar o projeto

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/inforgamer/MyMidiaList.git](https://github.com/inforgamer/MyMidiaList.git)
    ```
2.  **Abra no Android Studio:**
    Certifique-se de estar usando a versão **Koala** ou superior.
3.  **Sincronize o Gradle:**
    O projeto utiliza o Catálogo de Versões (`libs.versions.toml`).
4.  **Execute:**
    Conecte um dispositivo físico ou emulador (Recomendado API 35+).

---

## 🚧 Melhorias Futuras
- [ ] Adicionar sistema de backup na nuvem (Firebase).
- [ ] Modo Escuro/Claro automático.
- [ ] Porte para IOS

---

## 👨‍💻 Autor

Desenvolvido com tempo e café por **Infor** e como sub designer **Lucas1Black**.
