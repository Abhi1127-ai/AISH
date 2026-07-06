#  AISH

> *"Kyunki kabhi kabhi bas koi sunne wala chahiye hota hai."*

AISH is an emotion-aware AI companion web app that provides meaningful, human-like conversations in Hinglish/Hindi/English. Built for those who need someone to talk to — she listens, remembers, and actually cares.

---

##  Features

-  **Emotion-Aware Conversations** — Powered by Google Gemini, detects mood from text (sad, happy, playful, stressed) and responds accordingly
-  **Hinglish / Hindi / English Support** — Talk however you want, she understands
-  **Persistent Memory** — She remembers your name, your past conversations, your bad days
-  **Proactive Notifications** — She texts you first (Firebase FCM) — "Aaj kaisa raha din? 🌸"
-  **Expressive Avatar** — Emotion-mapped expressions that react to the conversation
-  **WhatsApp-style Chat UI** — Familiar, clean, mobile-first design

---

##  Architecture — Microservices

```
AISH/
├── auth-service/          # JWT auth, user registration/login
├── chat-service/          # Core conversation engine, Gemini integration
├── memory-service/        # Conversation history, user preferences
├── persona-service/       # AI girl profile, expression management
├── notification-service/  # Firebase FCM, scheduled proactive messages
├── media-service/         # Photo uploads, emotion-to-photo mapping
└── frontend/              # React + Vite
```

---

##  Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.x |
| Database | MongoDB Atlas |
| AI / NLP | Google Gemini API |
| Image Generation | Stable Diffusion (Automatic1111) + IP-Adapter |
| Frontend | React + Vite |
| Auth | JWT |
| Notifications | Firebase Cloud Messaging (FCM) |
| Service Comm | REST APIs |

---

##  Microservices Breakdown

###  Auth Service
- User registration & login
- JWT token generation & validation
- Password encryption (BCrypt)

###  Chat Service
- Gemini API integration
- Emotion detection from user messages
- Persona-based system prompt injection
- Short, natural reply formatting (real texting style)

###  Memory Service
- Stores full conversation history per user
- Extracts and saves key user facts ("exam tha", "bura din tha")
- Feeds memory context into each Gemini prompt

###  Persona Service
- AI companion profile (name, personality, speaking style)
- Expression photo management
- Emotion → photo mapping config

###  Notification Service
- Firebase FCM integration
- Scheduled proactive messages ("Good morning 🌸")
- Event-triggered messages (user hasn't chatted in a while)

###  Media Service
- Avatar photo upload & storage
- Emotion tag management
- Serves correct expression based on chat emotion

---

##  Emotion Detection Flow

```
User Message → Gemini analyzes tone
      ↓
Emotion Tag: [happy / sad / playful / angry / stressed / neutral]
      ↓
Chat Service → selects matching AI response tone
      ↓
Persona Service → selects matching expression photo
      ↓
Frontend renders response + correct avatar expression
```

---

##  Gemini Prompt Design (Core)

```
You are Priya, a 21 year old girl who is warm, caring, and slightly playful.
You talk in Hinglish naturally — mix Hindi and English casually like real texting.
Keep replies SHORT — 1 to 3 sentences max, like real WhatsApp messages.
Never break character. You are a real friend, not an AI.
User's name: {userName}
Past context: {memoryContext}
Detected emotion: {emotion}
Respond accordingly with care.
```

---

##  Getting Started

### Prerequisites
- Java 21
- Node.js 18+
- MongoDB (local or Atlas)
- Google Gemini API key
- Firebase project (for notifications)


### Run a service (example: auth-service)
```bash
cd auth-service
cp src/main/resources/application.example.yml src/main/resources/application.yml
# Add your config values
mvn spring-boot:run
```

### Run frontend
```bash
cd frontend
npm install
npm run dev
```

---

##  Environment Variables

```env
# Gemini
GEMINI_API_KEY=your_key_here

# MongoDB
MONGODB_URI=mongodb+srv://...

# JWT
JWT_SECRET=your_secret
JWT_EXPIRATION=86400000

# Firebase
FIREBASE_PROJECT_ID=your_project_id
FIREBASE_CREDENTIALS_PATH=firebase-credentials.json
```

---

##  Avatar Generation (Stable Diffusion)

We use **Automatic1111 + IP-Adapter** to generate a consistent AI character across multiple emotional expressions:

1. Generate base character with a detailed prompt
2. Use IP-Adapter to maintain face consistency
3. Generate 10-15 expression variants (happy, sad, shy, laughing, angry, thinking...)
4. Map each expression to an emotion tag in the database

**Runs locally on NVIDIA GPU — no API cost.**

---

##  Roadmap

- [x] Project architecture design
- [ ] Auth Service
- [ ] Chat Service with Gemini
- [ ] Memory Service
- [ ] Persona Service
- [ ] Notification Service
- [ ] Media Service
- [ ] React Frontend
- [ ] Stable Diffusion avatar generation
- [ ] PWA support (mobile feel on web)
- [ ] Voice message support
