# README : Package Player

## Table des Matières
- [Introduction](#introduction)
- [Structure du Package](#structure-du-package)
- [Classes Principales](#classes-principales)
    - [Player](#player)
    - [États](#états)
    - [Animations](#animations)
    - [Composants](#composants)
    - [Rendu](#rendu)
    - [Utilitaires](#utilitaires)
- [Constantes](#constantes)
- [Utilisation](#utilisation)

---

## Introduction
Le **package Player** est un module central du jeu RPG. Il gère la représentation, le comportement, les animations, les états et les interactions du personnage joueur dans le monde du jeu. Ce package fournit un cadre complet pour :
- Le déplacement (marche, course).
- Les transitions d'état (debout, marche, attaque).
- La gestion des animations.
- La détection des collisions.
- Les outils et équipements du joueur.

---

## Structure du Package
Le package est structuré comme suit :
```
player/                             // Package principal pour le joueur
├── animations/                     // Gestion des animations du joueur
│   ├── spritesheet/                // Traitement des spritesheets
│   │   ├── BodySpriteSheet.java    // Spritesheet pour le corps
│   │   ├── HairSpriteSheet.java    // Spritesheet pour les cheveux
│   │   ├── HatSpriteSheet.java     // Spritesheet pour le chapeau
│   │   ├── OutfitSpriteSheet.java  // Spritesheet pour la tenue
│   │   └── SpriteSheet.java        // Classe de base pour gérer les spritesheets
│   ├── AnimationController.java    // Gestion des animations du joueur
│   ├── AnimationState.java         // États d'animation (marche, attaque, etc.)
│   ├── CustomAnimation.java        // Classe pour les animations personnalisées
│   └── Direction.java              // Enum pour la direction du joueur
├── attacks/                        // Gestion des attaques du joueur
│   └── Attack.java                 // Classe pour les attaques
├── audio/                          // Gestion du son
│   └── SoundManager.java           // Classe pour gérer les sons
├── components/                     // Composants du joueur (cheveux, tenue, etc.)
│   ├── Body.java                   // Composant pour le corps
│   ├── CollisionComponent          // Composant de collision
│   ├── Hair.java                   // Composant pour les cheveux
│   ├── Hat.java                    // Composant pour le chapeau
│   ├── Outfit.java                 // Composant pour la tenue
│   ├── Tool1.java                  // Composant pour l'outil 1
│   └── Tool2.java                  // Composant pour l'outil 2
├── equipment/                      // Gestion de l'équipement
│   └── Tool.java                   // Classe de base pour les outils
├── rendering/                      // Rendu graphique
│   └── Renderer.java               // Classe principale pour le rendu
├── states/                         // États du joueur (marche, attaque, etc.)
│   ├── AttackingState.java         // État d'attaque
│   ├── RunningState                // État de course
│   ├── StandingState.java          // État de repos
│   ├── State.java                  // Interface ou classe abstraite de base pour les états
│   └── WalkingState.jave           // État de marche
├── utils/                          // Utilitaires divers pour le joueur
│   └── Constants.java              // Constantes pour faciliter la gestion
└── Player.java                     // Classe principale du joueur
```

---

## Classes Principales

### Player
La classe `Player` est le composant central qui :
- Gère la position, la vitesse et l'échelle du joueur.
- Traite les entrées utilisateur pour les déplacements et les interactions avec les outils.
- Gère l'état du joueur (par exemple, en attaque ou en marche).
- Intègre les animations et la détection des collisions.
- Fournit des méthodes pour le rendu et la mise à jour du joueur.

#### Méthodes Principales :
- **`update(float deltaTime, CollisionManager collisionManager)`** : Met à jour l'état, les animations et les délais des attaques du joueur.
- **`render(Batch batch)`** : Affiche le joueur à l'écran.
- **`changeState(State newState)`** : Change l'état actuel du joueur.
- **`move(float deltaTime, CollisionManager collisionManager)`** : Gère les déplacements du joueur en tenant compte des collisions.

#### Attributs Clés :
- **Position** (`x`, `y`) et **Vitesse**.
- **Composants** (par exemple, `Body`, `Hair`, `Outfit`).
- **État Actuel** (par exemple, `StandingState`, `AttackingState`).
- **Outils** et **Ensembles d'Outils**.
- **Animation Controller** et **Renderer**.

---

### États
Le package `states` définit les différents états du joueur. Chaque état implémente un comportement spécifique et des transitions.

#### Classes :
1. **`State`** (Classe Abstraite) :
    - Méthodes principales : `enter()`, `update(float deltaTime, CollisionManager collisionManager)`, `exit()`.
    - Fonctionnalité commune pour la gestion des états.

2. **`StandingState`** : État par défaut lorsque le joueur est immobile.
3. **`WalkingState`** : Gère les mécaniques de marche.
4. **`RunningState`** : Hérite de `WalkingState`, ajoute une vitesse accrue.
5. **`AttackingState`** : Gère les attaques du joueur et les transitions associées.

---

### Animations
Le package `animations` gère la logique des animations.

#### Classes :
1. **`AnimationController`** :
    - Met à jour les animations en fonction de l'état et du mouvement du joueur.
    - Gère les transitions entre les animations (par exemple, de marche à course).
2. **`AnimationState`** :
    - Enum définissant les états d'animation (par exemple, `WALKING_LEFT`, `STANDING_DOWN`).

---

### Composants
Le package `components` encapsule les aspects visuels et fonctionnels du joueur.

#### Classes :
1. **`Body`** : Représentation visuelle de base du joueur.
2. **`Outfit`** : Gère les vêtements et la personnalisation visuelle du joueur.
3. **`Hair`** : Ajoute des coiffures au joueur.
4. **`Hat`** : Gère les couvre-chefs.
5. **`Tool1`** et **`Tool2`** : Représentent les outils primaires et secondaires (armes, boucliers).

---

### Rendu
Le package `rendering` garantit que le joueur est correctement représenté visuellement à l'écran.

#### Classes :
1. **`Renderer`** :
    - Gère la logique de rendu pour tous les composants (par exemple, `Body`, `Hair`, `Outfit`).
    - Assure le bon agencement des éléments visuels (par exemple, le chapeau au-dessus des cheveux).

---

### Utilitaires
Le package `utils` contient des constantes et des valeurs utilitaires pour le joueur.

#### Classes :
1. **`Constants`** :
    - Définit des constantes essentielles telles que `PLAYER_WALKING_SPEED`, `PLAYER_RUNNING_SPEED`, les dimensions de la boîte de collision, et les IDs d'outils par défaut.

---

## Constantes
Les constantes clés dans la classe `Constants` incluent :
- **Attributs du Joueur** :
    - `PLAYER_WALKING_SPEED`
    - `PLAYER_RUNNING_SPEED`
    - `PLAYER_COLLISION_WIDTH`
    - `PLAYER_COLLISION_HEIGHT`

- **Outils et Attaques** :
    - IDs d'outils par défaut (par exemple, `"SW01"` pour une épée, `"SH01"` pour un bouclier).

---

## Utilisation

### Initialisation
```java
Player player = new Player(startX, startY, scale);
```

### Mise à Jour de l'État du Joueur
```java
player.update(deltaTime, collisionManager);
```

### Rendu du Joueur
```java
player.render(batch);
```

### Changement d'Outils
```java
player.switchToolSet(toolSetIndex);
```

### Gestion des États
```java
player.changeState(new AttackingState(player, attack, tool));
```
