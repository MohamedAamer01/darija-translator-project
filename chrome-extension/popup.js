// Configuration
const API_URL = 'http://localhost:8080/darija-translator/api/translator/translate';

// Éléments DOM
const inputText = document.getElementById('inputText');
const outputText = document.getElementById('outputText');
const translateBtn = document.getElementById('translateBtn');
const clearBtn = document.getElementById('clearBtn');
const swapBtn = document.getElementById('swapBtn');
const copyBtn = document.getElementById('copyBtn');
const languageMode = document.getElementById('languageMode');
const status = document.getElementById('status');
const charCount = document.getElementById('charCount');
const exampleItems = document.querySelectorAll('.example-item');

// État
let isEnglishToDarija = true;

// Event Listeners
inputText.addEventListener('input', updateCharCount);
translateBtn.addEventListener('click', translate);
clearBtn.addEventListener('click', clearAll);
swapBtn.addEventListener('click', swapLanguages);
copyBtn.addEventListener('click', copyToClipboard);
languageMode.addEventListener('change', updateLanguageMode);

// Examples
exampleItems.forEach(item => {
    item.addEventListener('click', () => {
        inputText.value = item.dataset.text;
        updateCharCount();
    });
});

// Fonctions
function updateCharCount() {
    const count = inputText.value.length;
    charCount.textContent = `${count} caractères`;
    charCount.style.color = count > 1000 ? '#ef4444' : '#6b7280';
}

function updateLanguageMode() {
    isEnglishToDarija = languageMode.value === 'en-to-darija';
    
    if (isEnglishToDarija) {
        inputText.style.direction = 'ltr';
        inputText.style.textAlign = 'left';
        outputText.style.direction = 'rtl';
        outputText.style.textAlign = 'right';
    } else {
        inputText.style.direction = 'rtl';
        inputText.style.textAlign = 'right';
        outputText.style.direction = 'ltr';
        outputText.style.textAlign = 'left';
    }
    
    showStatus('Mode changé', 'success');
}

async function translate() {
    const text = inputText.value.trim();
    
    if (!text) {
        showStatus('Veuillez entrer un texte', 'error');
        return;
    }
    
    // Désactiver les boutons
    setButtonsEnabled(false);
    showStatus('Traduction en cours...', 'loading');
    outputText.value = '';
    copyBtn.disabled = true;
    
    try {
        // Préparer le prompt selon la direction
        let prompt;
        if (isEnglishToDarija) {
            prompt = `Translate the following English text to Moroccan Darija. ` +
                    `IMPORTANT: Write the translation ONLY in Arabic script (not Latin letters). ` +
                    `Use Arabic letters: ا، ب، ت، ث، etc. ` +
                    `Text: ${text}`;
        } else {
            prompt = `Translate the following Moroccan Darija text (in Arabic script) to English. ` +
                    `Text: ${text}`;
        }
        
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ text: prompt })
        });
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        
        const data = await response.json();
        outputText.value = data.translation;
        copyBtn.disabled = false;
        
        showStatus(`✓ Traduction réussie (${data.mode})`, 'success');
        
    } catch (error) {
        showStatus(`✗ Erreur: ${error.message}`, 'error');
        console.error('Translation error:', error);
    } finally {
        setButtonsEnabled(true);
    }
}

function clearAll() {
    inputText.value = '';
    outputText.value = '';
    copyBtn.disabled = true;
    charCount.textContent = '0 caractères';
    showStatus('Prêt à traduire', '');
}

function swapLanguages() {
    const temp = inputText.value;
    inputText.value = outputText.value;
    outputText.value = temp;
    
    languageMode.value = isEnglishToDarija ? 'darija-to-en' : 'en-to-darija';
    updateLanguageMode();
    
    showStatus('↔ Langues inversées', 'success');
}

function copyToClipboard() {
    const text = outputText.value;
    if (text) {
        navigator.clipboard.writeText(text).then(() => {
            showStatus('✓ Texte copié', 'success');
            
            // Animation flash
            copyBtn.style.background = '#22c55e';
            setTimeout(() => {
                copyBtn.style.background = '';
            }, 200);
        }).catch(err => {
            showStatus('✗ Erreur de copie', 'error');
        });
    }
}

function setButtonsEnabled(enabled) {
    translateBtn.disabled = !enabled;
    clearBtn.disabled = !enabled;
    swapBtn.disabled = !enabled;
    
    if (!enabled) {
        translateBtn.classList.add('loading');
    } else {
        translateBtn.classList.remove('loading');
    }
}

function showStatus(message, type) {
    status.textContent = message;
    status.className = 'status';
    if (type) {
        status.classList.add(type);
    }
}

// Initialisation
updateCharCount();