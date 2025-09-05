# AI Chatbot Application

A modern AI chatbot application built with Java Swing, featuring a clean dark-themed interface and integration with Google's Gemini API.

## Features

- **Modern Dark UI**: Clean, professional interface using FlatLaf
- **User Authentication**: Secure login and registration system
- **AI Integration**: Powered by Google Gemini API for intelligent conversations
- **Voice Support**: Voice input and text-to-speech capabilities
- **Chat History**: Persistent storage and retrieval of conversations
- **User Settings**: Customizable voice preferences and settings
- **Responsive Design**: Optimized for different screen sizes

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Google Gemini API key

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd java_project
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure Gemini API**
   - Get your API key from [Google AI Studio](https://makersuite.google.com/app/apikey)
   - Set the API key as a system property:
     ```bash
     java -DGEMINI_API_KEY=your-api-key-here -jar target/ai-chatbot-1.0.0.jar
     ```

4. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="com.chatbot.Main"
   ```

   Or build and run the JAR:
   ```bash
   mvn package
   java -DGEMINI_API_KEY=your-api-key-here -jar target/ai-chatbot-1.0.0.jar
   ```

## Usage

1. **Registration**: Create a new account with username and password
2. **Login**: Sign in with your credentials
3. **Chat**: Type messages or use the microphone button for voice input
4. **History**: View previous conversations using the History button
5. **Settings**: Customize voice preferences and other options

## Architecture

- **Database**: SQLite for local data storage
- **UI Framework**: Java Swing with FlatLaf for modern appearance
- **AI Service**: Google Gemini API integration
- **Voice Service**: Built-in Java audio capabilities
- **Security**: Password hashing with salt for secure authentication

## Project Structure

```
src/main/java/com/chatbot/
├── Main.java                    # Application entry point
├── database/
│   └── DatabaseManager.java    # Database operations
├── model/
│   ├── User.java               # User data model
│   └── ChatMessage.java        # Chat message model
├── service/
│   ├── GeminiService.java      # AI service integration
│   └── VoiceService.java       # Voice input/output
├── ui/
│   ├── LoginFrame.java         # Login interface
│   ├── RegisterFrame.java      # Registration interface
│   ├── MainFrame.java          # Main chat interface
│   ├── ChatHistoryDialog.java  # Chat history viewer
│   ├── SettingsDialog.java     # Settings configuration
│   └── components/             # Custom UI components
└── util/
    └── PasswordUtil.java       # Password hashing utilities
```

## Features in Detail

### Authentication System
- Secure password hashing with SHA-256 and salt
- User registration with validation
- Session management

### AI Integration
- Real-time conversation with Gemini API
- Error handling and fallback responses
- Conversation context preservation

### Voice Features
- Microphone input for voice messages
- Text-to-speech for AI responses
- Configurable voice speed settings

### Data Persistence
- SQLite database for user data and chat history
- Automatic database initialization
- Chat history with timestamps

### Modern UI
- Dark theme with professional appearance
- Smooth animations and hover effects
- Responsive layout for different screen sizes
- Custom styled components

## Customization

### Themes
The application uses FlatLaf's dark theme. You can modify colors in the UI components or switch to light theme by changing the look and feel in `Main.java`.

### API Integration
To use a different AI service, implement a new service class following the pattern in `GeminiService.java`.

### Voice Services
The voice functionality can be enhanced by integrating with external speech-to-text and text-to-speech services.

## Troubleshooting

### Common Issues

1. **Database Connection**: Ensure write permissions in the application directory
2. **API Key**: Verify your Gemini API key is valid and properly set
3. **Audio Issues**: Check microphone permissions and audio device availability

### Logging
The application logs errors to the console. Enable debug logging by adding logging configuration.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Google Gemini API for AI capabilities
- FlatLaf for modern UI components
- SQLite for embedded database functionality