class GramaConnect {
    constructor() {
        this.currentUser = null;
        this.currentScreen = 'loginScreen';
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.setupTabSwitching();
        this.showScreen('loginScreen');
    }

    setupEventListeners() {
        // Login form
        const loginForm = document.getElementById('loginForm');
        loginForm.addEventListener('submit', (e) => this.handleLogin(e));

        // Logout button
        const logoutBtn = document.getElementById('logoutBtn');
        logoutBtn.addEventListener('click', () => this.handleLogout());

        // Dashboard cards
        const dashboardCards = document.querySelectorAll('.dashboard-card');
        dashboardCards.forEach(card => {
            card.addEventListener('click', () => {
                const module = card.dataset.module;
                if (module) {
                    this.navigateToModule(module);
                }
            });
        });

        // Back buttons
        const backBtns = document.querySelectorAll('.back-btn');
        backBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                const target = btn.dataset.target;
                this.showScreen(target);
            });
        });

        // Job search and filters
        const jobSearch = document.getElementById('jobSearch');
        if (jobSearch) {
            jobSearch.addEventListener('input', (e) => this.filterJobs(e.target.value));
        }

        const jobFilters = document.querySelectorAll('.filter-btn');
        jobFilters.forEach(btn => {
            btn.addEventListener('click', (e) => {
                jobFilters.forEach(f => f.classList.remove('active'));
                e.target.classList.add('active');
                this.filterJobsByCategory(e.target.dataset.filter);
            });
        });

        // Form submissions
        const cropReportForm = document.querySelector('.crop-report-form');
        if (cropReportForm) {
            cropReportForm.addEventListener('submit', (e) => this.handleCropReport(e));
        }

        // Apply buttons
        const applyBtns = document.querySelectorAll('.job-card .btn-outline');
        applyBtns.forEach(btn => {
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                this.showToast('Application submitted successfully!', 'success');
            });
        });
    }

    setupTabSwitching() {
        const tabBtns = document.querySelectorAll('.tab-btn');
        const tabContents = document.querySelectorAll('.tab-content');

        tabBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                // Remove active class from all tabs and content
                tabBtns.forEach(tab => tab.classList.remove('active'));
                tabContents.forEach(content => content.classList.remove('active'));

                // Add active class to clicked tab
                btn.classList.add('active');

                // Show corresponding content
                const targetTab = btn.dataset.tab;
                const targetContent = document.querySelector(`[data-content="${targetTab}"]`);
                if (targetContent) {
                    targetContent.classList.add('active');
                }
            });
        });
    }

    handleLogin(e) {
        e.preventDefault();

        const formData = new FormData(e.target);
        const username = formData.get('username');
        const password = formData.get('password');
        const userRole = formData.get('userRole');

        // Simple validation (in real app, this would be server-side)
        if (!username || !password || !userRole) {
            this.showToast('Please fill all fields', 'error');
            return;
        }

        // Mock authentication
        this.currentUser = {
            username: username,
            role: userRole
        };

        // Update UI based on user role
        this.updateUserInterface();

        // Show dashboard
        this.showScreen('dashboardScreen');
        this.showToast(`Welcome ${username}!`, 'success');
    }

    handleLogout() {
        this.currentUser = null;
        document.body.className = '';
        this.showScreen('loginScreen');
        this.showToast('Logged out successfully', 'success');
    }

    updateUserInterface() {
        if (!this.currentUser) return;

        // Update welcome message
        const welcomeMessage = document.getElementById('welcomeMessage');
        const userRoleBadge = document.getElementById('userRoleBadge');

        welcomeMessage.textContent = `Welcome, ${this.currentUser.username}!`;
        userRoleBadge.textContent = this.capitalizeFirst(this.currentUser.role.replace('-', ' '));

        // Add role-based class to body
        document.body.className = `user-${this.currentUser.role}`;

        // Show/hide role-specific content
        this.updateRoleBasedContent();
    }

    updateRoleBasedContent() {
        const roleElements = {
            'farmer-only': this.currentUser.role === 'farmer',
            'admin-only': this.currentUser.role === 'admin',
            'health-worker-only': this.currentUser.role === 'health-worker'
        };

        Object.keys(roleElements).forEach(className => {
            const elements = document.querySelectorAll(`.${className}`);
            elements.forEach(element => {
                if (roleElements[className]) {
                    element.style.display = '';
                } else {
                    element.style.display = 'none';
                }
            });
        });
    }

    navigateToModule(module) {
        const moduleScreenMap = {
            'notices': 'noticesScreen',
            'farming': 'farmingScreen',
            'health': 'healthScreen',
            'jobs': 'jobsScreen',
            'education': 'educationScreen',
            'market': 'marketScreen',
            'admin': 'adminScreen'
        };

        const screenId = moduleScreenMap[module];
        if (screenId) {
            // Check if user has access to this module
            if (this.hasAccess(module)) {
                this.showScreen(screenId);
            } else {
                this.showToast('Access denied for this module', 'error');
            }
        }
    }

    hasAccess(module) {
        if (!this.currentUser) return false;

        const rolePermissions = {
            'villager': ['notices', 'health', 'jobs', 'education', 'market'],
            'farmer': ['notices', 'farming', 'health', 'jobs', 'education', 'market'],
            'health-worker': ['notices', 'health', 'jobs', 'education', 'market'],
            'teacher': ['notices', 'health', 'jobs', 'education', 'market'],
            'admin': ['notices', 'farming', 'health', 'jobs', 'education', 'market', 'admin']
        };

        const userPermissions = rolePermissions[this.currentUser.role] || [];
        return userPermissions.includes(module);
    }

    showScreen(screenId) {
        // Hide all screens
        const screens = document.querySelectorAll('.screen');
        screens.forEach(screen => screen.classList.remove('active'));

        // Show target screen
        const targetScreen = document.getElementById(screenId);
        if (targetScreen) {
            targetScreen.classList.add('active');
            this.currentScreen = screenId;
        }
    }

    filterJobs(searchTerm) {
        const jobCards = document.querySelectorAll('.job-card');
        const term = searchTerm.toLowerCase();

        jobCards.forEach(card => {
            const title = card.querySelector('h3').textContent.toLowerCase();
            const description = card.querySelector('.job-description').textContent.toLowerCase();

            if (title.includes(term) || description.includes(term)) {
                card.style.display = '';
            } else {
                card.style.display = 'none';
            }
        });
    }

    filterJobsByCategory(category) {
        const jobCards = document.querySelectorAll('.job-card');

        jobCards.forEach(card => {
            if (category === 'all' || card.dataset.category === category) {
                card.style.display = '';
            } else {
                card.style.display = 'none';
            }
        });
    }

    handleCropReport(e) {
        e.preventDefault();

        // Simulate form submission
        setTimeout(() => {
            this.showToast('Crop issue reported successfully! Our experts will review it.', 'success');
            e.target.reset();
        }, 500);
    }

    showToast(message, type = 'success') {
        const container = document.getElementById('toastContainer');

        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.textContent = message;

        container.appendChild(toast);

        // Auto remove after 5 seconds
        setTimeout(() => {
            toast.remove();
        }, 5000);

        // Add click to dismiss
        toast.addEventListener('click', () => toast.remove());
    }

    capitalizeFirst(str) {
        return str.charAt(0).toUpperCase() + str.slice(1);
    }
}

// Weather simulation
class WeatherService {
    static updateWeather() {
        const conditions = [
            { icon: '☀️', temp: '28°C', condition: 'Sunny' },
            { icon: '⛅', temp: '25°C', condition: 'Partly Cloudy' },
            { icon: '🌧️', temp: '23°C', condition: 'Rainy' },
            { icon: '🌤️', temp: '26°C', condition: 'Mostly Sunny' }
        ];

        const weather = conditions[Math.floor(Math.random() * conditions.length)];

        const weatherIcon = document.querySelector('.weather-icon');
        const temp = document.querySelector('.temp');
        const condition = document.querySelector('.condition');

        if (weatherIcon) weatherIcon.textContent = weather.icon;
        if (temp) temp.textContent = weather.temp;
        if (condition) condition.textContent = weather.condition;
    }
}

// Statistics simulation
class StatsService {
    static updateStats() {
        const stats = {
            users: Math.floor(Math.random() * 500) + 1000,
            notices: Math.floor(Math.random() * 10) + 10,
            jobs: Math.floor(Math.random() * 15) + 15,
            health: Math.floor(Math.random() * 5) + 5
        };

        // Update notice counts
        const noticesCount = document.getElementById('noticesCount');
        if (noticesCount) {
            noticesCount.textContent = `${Math.floor(Math.random() * 8) + 3} new notices`;
        }

        // Update admin stats
        const statElements = document.querySelectorAll('.stat-number');
        const statValues = Object.values(stats);
        statElements.forEach((element, index) => {
            if (statValues[index]) {
                element.textContent = statValues[index].toLocaleString();
            }
        });
    }
}

// Multilingual support
class LanguageService {
    static languages = {
        en: {
            welcome: 'Welcome to GramaConnect',
            tagline: 'Connecting villages, empowering communities'
        },
        si: {
            welcome: 'GramaConnect වෙත සාදරයෙන් පිළිගනිමු',
            tagline: 'ගම් සම්බන්ධ කරමින්, ප්‍රජාවන් සවිබල ගන්වමින්'
        },
        ta: {
            welcome: 'GramaConnect இல் உங்களை வரவேற்கிறோம்',
            tagline: 'கிராமங்களை இணைத்து, சமூகங்களை வலுப்படுத்துகிறோம்'
        }
    };

    static currentLanguage = 'en';

    static switchLanguage(lang) {
        this.currentLanguage = lang;
        // Implementation for language switching would go here
        console.log(`Language switched to: ${lang}`);
    }
}

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
    const app = new GramaConnect();

    // Update weather every 30 seconds
    setInterval(() => {
        WeatherService.updateWeather();
    }, 30000);

    // Update stats every 60 seconds
    setInterval(() => {
        StatsService.updateStats();
    }, 60000);

    // Initial updates
    WeatherService.updateWeather();
    StatsService.updateStats();

    // Add some demo notifications
    setTimeout(() => {
        if (document.getElementById('dashboardScreen').classList.contains('active')) {
            app.showToast('New weather alert available', 'warning');
        }
    }, 3000);
});

// Service Worker registration for offline capability
if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('/sw.js')
            .then((registration) => {
                console.log('SW registered: ', registration);
            })
            .catch((registrationError) => {
                console.log('SW registration failed: ', registrationError);
            });
    });
}
// Store registered users in localStorage
let users = JSON.parse(localStorage.getItem("users")) || [
    { username: "admin", password: "123", role: "admin" }
];

// Registration
function registerUser() {
    const username = document.getElementById("regUsername").value.trim();
    const password = document.getElementById("regPassword").value.trim();
    const role = document.getElementById("regRole").value;

    if (!username || !password) {
        showToast("Please fill all fields!");
        return;
    }

    if (users.find(u => u.username === username)) {
        showToast("Username already exists!");
        return;
    }

    users.push({ username, password, role });
    localStorage.setItem("users", JSON.stringify(users));

    showToast("Account created! Please login.");
    showScreen("loginScreen");
}

// Login
function login() {
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    const user = users.find(u => u.username === username && u.password === password);

    if (user) {
        currentUser = user;
        showToast(`Welcome ${user.username}`);
        showScreen("dashboardScreen");
        updateDashboardAccess(user.role);
    } else {
        showToast("Invalid credentials!");
    }
}

// Add skill to market
function addSkill() {
    const skillName = document.getElementById("skillName").value.trim();
    const skillPrice = document.getElementById("skillPrice").value.trim();
    if (!skillName || !skillPrice) {
        showToast("Please fill details!");
        return;
    }

    const ul = document.querySelector("#marketScreen ul");
    const li = document.createElement("li");
    li.textContent = `🛠️ ${skillName} - Rs. ${skillPrice}`;
    ul.appendChild(li);

    document.getElementById("skillName").value = "";
    document.getElementById("skillPrice").value = "";
    showToast("Skill added!");
}
