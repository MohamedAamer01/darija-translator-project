# 🌍 Traducteur English ↔ Darija

Traducteur intelligent utilisant l'API Gemini pour traduire entre l'anglais et le darija marocain (dialecte arabe du Maroc).

## 📋 Description

Ce projet est composé de trois parties :
- **Backend** : API REST Java (Jakarta EE) qui communique avec l'API Gemini
- **Client Desktop** : Application Java Swing avec interface graphique moderne
- **Extension Chrome** : Extension de navigateur pour traduction rapide

## ✨ Fonctionnalités

- ✅ Traduction bidirectionnelle (English ↔ Darija)
- ✅ Interface moderne et intuitive
- ✅ Support de l'écriture arabe (RTL)
- ✅ Mode simulation pour tests sans API
- ✅ Copier/Coller rapide
- ✅ Exemples intégrés

## 🛠️ Technologies utilisées

### Backend
- Java 17+
- Jakarta EE 10
- JAX-RS (REST API)
- JSON Processing
- Google Gemini API

### Frontend Desktop
- Java Swing
- HTTP Client (Java 11+)

### Extension Chrome
- HTML5 / CSS3
- JavaScript (ES6+)
- Chrome Extension Manifest V3

## 📦 Installation

### Prérequis
- JDK 17 ou supérieur
- Apache WiFly
- Google Chrome
- Clé API Gemini (gratuite)

### Backend (API Java)

1. **Clonez le repository**
```bash
git clone https://github.com/MohamedAamer01/darija-translator-project.git
cd darija-translator-project/backend
```

2. **Configurez votre clé API Gemini**
   - Obtenez une clé sur : https://aistudio.google.com/
   - Remplacez dans `TranslatorResource.java` :
```java
   private final String GEMINI_API_KEY = "VOTRE_CLE_ICI";
```

3. **Déployez sur WidFly**
   - Importez le projet dans Eclipse/IntelliJ
   - Configurez WidFly
   - Démarrez le serveur

4. **L'API sera accessible sur** : `http://localhost:8080/darija-translator/api/translator/translate`

### Client Desktop (Java Swing)

1. **Compilez et exécutez**
```bash
cd backend/src
javac ma/project/jakarta/client/TranslatorGUI.java
java ma.project.jakarta.client.TranslatorGUI
```

Ou exécutez directement depuis votre IDE.

### Extension Chrome

1. **Ouvrez Chrome** et allez sur : `chrome://extensions/`

2. **Activez le "Mode développeur"** (en haut à droite)

3. **Cliquez sur "Charger l'extension non empaquetée"**

4. **Sélectionnez** le dossier `chrome-extension/`

5. **L'extension est installée** ! Cliquez sur l'icône pour l'utiliser.

## 🚀 Utilisation

### API REST

**Endpoint** : `POST /api/translator/translate`

**Request Body** :
```json
{
  "text": "Hello"
}
```

**Response** :
```json
{
  "original": "Hello",
  "translation": "السلام",
  "mode": "gemini"
}
```

### Application Desktop

1. Lancez l'application
2. Tapez votre texte
3. Sélectionnez la direction (English → Darija ou inverse)
4. Cliquez sur "Traduire"
5. Utilisez "Copier" pour copier le résultat

### Extension Chrome

1. Cliquez sur l'icône de l'extension
2. Entrez votre texte
3. Cliquez sur "Traduire"
4. Le résultat s'affiche instantanément

## 📸 Captures d'écran

### Application Desktop
![Application Java Swing](screenshots/app-java.png)

### Extension Chrome
![Extension Chrome](screenshots/extension-chrome.png)

## 🔧 Configuration avancée

### Mode Simulation (sans API Gemini)

Dans `TranslatorResource.java`, changez :
```java
private final boolean SIMULATION_MODE = true;
```

### Personnaliser l'URL de l'API

Dans l'extension Chrome, modifiez `popup.js` :
```javascript
const API_URL = 'http://VOTRE_SERVEUR:PORT/darija-translator/api/translator/translate';
```
## ⚠️ Configuration de la clé API

1. Obtenez une clé API Gemini gratuite sur : https://aistudio.google.com/
2. Dans `backend/src/ma/project/jakarta/api/TranslatorResource.java`
3. Remplacez `VOTRE_CLE_API_ICI` par votre vraie clé
##  Contribution

Les contributions sont les bienvenues ! 

1. Fork le projet
2. Créez votre branche (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📝 License

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 👨‍💻 Auteur

**Votre Nom**
- GitHub: [@AAMER_MOHAMED](https://github.com/MohamedAamer01)

## 🙏 Remerciements

- Google Gemini API pour la traduction
- Anthropic Claude pour l'assistance au développement
- La communauté open source

## 📞 Contact

Pour toute question ou suggestion : mohamedaamer0622@gmail.com

---

⭐ Si ce projet vous a aidé, n'hésitez pas à lui donner une étoile !

## 🎥 Vidéo de démonstration

[![Démo vidéo](screenshots/javaApp.png)](https://drive.google.com/file/d/11AAK1Kyd3m1n73qChmRJ0HXhAWH2Wdif/view?usp=sharing)

📹 **[Cliquez ici pour regarder la vidéo complète](https://drive.google.com/file/d/11AAK1Kyd3m1n73qChmRJ0HXhAWH2Wdif/view?usp=sharing)
