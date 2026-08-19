# Missale & Breviarium Romanum 1960 — Chant Grégorien

Application Android moderne, complète et autonome pour le **Missel Romain**, le **Bréviaire Romain (1960 / 1962)** et le **Chant Grégorien** (Graduale Romanum / Liber Usualis).

Conçue avec **Jetpack Compose** et le moteur SVG officiel **jgabc / Gregorio**, l'application propose une gravure grégorienne fidèle à la facture des éditions de **Solesmes** et aux partitions de [bastonus/jgabc](https://github.com/bastonus/jgabc).

---

## 🌟 Fonctionnalités Principales

### 🎵 Chant Grégorien & Partitions Interactives (Solesmes Style)
- **Gravure SVG officielle** : Partitions complètes du Propre de la Messe (Introit, Graduel, Alléluia, Trait, Offertoire, Communion, Séquences, Ordinaire).
- **Facture traditionnelle Solesmes (`modern_propers.html`)** :
  - Portées rouges à 4 lignes (ou noires au choix).
  - Lettrines / initiales ornées (Drop Caps 64px).
  - Annotations liturgiques et mode grégorien (ex. *Intr. VIII*).
  - Références bibliques et commentaires en rubriques rouges (*Ps. 24, 1-3*).
- **Mode Sombre complet** : Adaptation automatique avec notes et textes en blanc pur (`#FFFFFF`) et rubriques / portées en rouge liturgique (`#E57373`).
- **Donneur de ton (Pitch Pipe)** : Générateur audio de fréquences pour chaque ton naturel et bémolisé.
- **Export & Partage** : Copie directe du code source **GABC** et export de l'image vectorielle **SVG**.

### 📖 Missel Romain & Lectio
- Textes complets en **latin** et **français** pour les dimanches et fêtes de l'année liturgique 1960 / 1962.
- Oraisons (Collecte, Secrète, Postcommunion), Épîtres et Saint Évangile.
- Ordinaire de la Messe (*Ordo Missae*) avec rubriques détaillées.

### ⛪ Bréviaire & Heures Canoniales
- Offices complets : Matines, Laudes, Prime, Tierce, Sexte, None, Vêpres et Complies.
- Psaumes avec versets numérotés, accents toniques, médiantes (`*`) et flexes (`†`).
- Cantiques évangéliques (*Benedictus*, *Magnificat*, *Nunc Dimittis*).

### 📅 Calendrier Liturgique Perpétuel
- Calcul automatique des fêtes mobiles (Pâques, Ascension, Pentecôte, Fête-Dieu, etc.).
- Temps liturgiques (Avent, Temps de Noël, Septuagésime, Carême, Temps Pascal, Temps après la Pentecôte).
- Couleurs liturgiques (Blanc, Rouge, Vert, Violet, Noir, Rose).

---

## 📲 Téléchargement & Installation

1. Rendez-vous dans la section **[Releases](../../releases)** du dépôt GitHub.
2. Téléchargez le dernier fichier **`app-debug.apk`** ou **`app-release.apk`**.
3. Sur votre appareil Android (Android 7.0 / API 24 ou supérieur), ouvrez le fichier APK et autorisez l'installation d'applications de sources inconnues si nécessaire.

---

## 🛠️ Compilation & Développement

### Prérequis
- **JDK 21**
- **Android SDK** (API 34 / 35 / 36)
- **Gradle 9.3.1**

### Commandes utiles

```bash
# Compiler l'application en mode Debug
gradle :app:assembleDebug

# Lancer les tests unitaires et Robolectric
gradle :app:testDebugUnitTest

# Générer les captures d'écran Roborazzi
gradle :app:recordRoborazziDebug
```

---

## 📜 Licences et Remerciements

- Moteur de rendu GABC : [jgabc](https://github.com/bastonus/jgabc) & [Exsurge](https://github.com/bastonus/exsurge) par Benjamin Bloomfield.
- Textes liturgiques et données grégoriennes : Graduale Romanum, Liber Usualis et Solesmes.
- Développé sous licence open-source.
