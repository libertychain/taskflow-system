// TechAlves Soluções - Main JavaScript File
// Funcionalidades compartilhadas entre as páginas

class TechAlvesApp {
    constructor() {
        this.api = axios.create({
            baseURL: 'http://localhost:8080',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        this.sessionId = localStorage.getItem('sessionId');
        this.currentUser = JSON.parse(localStorage.getItem('user') || '{}');
        
        this.init();
    }
    
    init() {
        this.setupInterceptors();
        this.checkAuthentication();
    }
    
    setupInterceptors() {
        // Adicionar token de autenticação a todas as requisições
        this.api.interceptors.request.use((config) => {
            if (this.sessionId) {
                config.headers.Authorization = `Bearer ${this.sessionId}`;
            }
            return config;
        });
        
        // Tratar respostas de erro
        this.api.interceptors.response.use(
            (response) => response,
            (error) => {
                if (error.response?.status === 401) {
                    this.handleUnauthorized();
                }
                return Promise.reject(error);
            }
        );
    }
    
    async checkAuthentication() {
        if (!this.sessionId) {
            this.redirectToLogin();
            return false;
        }
        
        try {
            const response = await this.api.get('/api/auth/validate');
            if (!response.data.valid) {
                this.handleUnauthorized();
                return false;
            }
            return true;
        } catch (error) {
            this.handleUnauthorized();
            return false;
        }
    }
    
    handleUnauthorized() {
        localStorage.clear();
        this.redirectToLogin();
    }
    
    redirectToLogin() {
        if (window.location.pathname !== '/index.html' && window.location.pathname !== '/') {
            window.location.href = 'index.html';
        }
    }
    
    // Métodos utilitários
    formatDate(dateString) {
        if (!dateString) return 'Não definido';
        return new Date(dateString).toLocaleDateString('pt-BR');
    }
    
    formatDateTime(dateString) {
        if (!dateString) return 'Não definido';
        return new Date(dateString).toLocaleString('pt-BR');
    }
    
    getStatusColor(status) {
        const colors = {
            'PENDING': 'bg-yellow-400',
            'IN_PROGRESS': 'bg-blue-400',
            'COMPLETED': 'bg-green-400',
            'CANCELLED': 'bg-red-400',
            'ACTIVE': 'bg-blue-400',
            'ON_HOLD': 'bg-yellow-400'
        };
        return colors[status] || 'bg-gray-400';
    }
    
    getStatusBadgeColor(status) {
        const colors = {
            'PENDING': 'bg-yellow-100 text-yellow-800',
            'IN_PROGRESS': 'bg-blue-100 text-blue-800',
            'COMPLETED': 'bg-green-100 text-green-800',
            'CANCELLED': 'bg-red-100 text-red-800',
            'ACTIVE': 'bg-blue-100 text-blue-800',
            'ON_HOLD': 'bg-yellow-100 text-yellow-800'
        };
        return colors[status] || 'bg-gray-100 text-gray-800';
    }
    
    getStatusText(status) {
        const texts = {
            'PENDING': 'Pendente',
            'IN_PROGRESS': 'Em Progresso',
            'COMPLETED': 'Concluída',
            'CANCELLED': 'Cancelada',
            'ACTIVE': 'Ativo',
            'ON_HOLD': 'Em Espera'
        };
        return texts[status] || status;
    }
    
    getPriorityBadgeColor(priority) {
        const colors = {
            'LOW': 'bg-green-100 text-green-800',
            'MEDIUM': 'bg-yellow-100 text-yellow-800',
            'HIGH': 'bg-red-100 text-red-800',
            'URGENT': 'bg-red-200 text-red-900'
        };
        return colors[priority] || 'bg-gray-100 text-gray-800';
    }
    
    getPriorityText(priority) {
        const texts = {
            'LOW': 'Baixa',
            'MEDIUM': 'Média',
            'HIGH': 'Alta',
            'URGENT': 'Urgente'
        };
        return texts[priority] || priority;
    }
    
    // Métodos de notificação
    showNotification(message, type = 'info') {
        // Criar elemento de notificação
        const notification = document.createElement('div');
        notification.className = `fixed top-4 right-4 p-4 rounded-lg shadow-lg z-50 ${this.getNotificationClass(type)}`;
        notification.innerHTML = `
            <div class="flex items-center">
                <i class="fas ${this.getNotificationIcon(type)} mr-2"></i>
                <span>${message}</span>
                <button onclick="this.parentElement.parentElement.remove()" class="ml-4 text-gray-500 hover:text-gray-700">
                    <i class="fas fa-times"></i>
                </button>
            </div>
        `;
        
        document.body.appendChild(notification);
        
        // Remover após 5 segundos
        setTimeout(() => {
            if (notification.parentElement) {
                notification.remove();
            }
        }, 5000);
    }
    
    getNotificationClass(type) {
        const classes = {
            'success': 'bg-green-500 text-white',
            'error': 'bg-red-500 text-white',
            'warning': 'bg-yellow-500 text-white',
            'info': 'bg-blue-500 text-white'
        };
        return classes[type] || classes.info;
    }
    
    getNotificationIcon(type) {
        const icons = {
            'success': 'fa-check-circle',
            'error': 'fa-exclamation-circle',
            'warning': 'fa-exclamation-triangle',
            'info': 'fa-info-circle'
        };
        return icons[type] || icons.info;
    }
    
    // Métodos de loading
    showLoading(element) {
        if (typeof element === 'string') {
            element = document.getElementById(element);
        }
        
        if (element) {
            element.innerHTML = `
                <div class="flex items-center justify-center py-8">
                    <i class="fas fa-spinner fa-spin text-2xl text-blue-500 mr-2"></i>
                    <span class="text-gray-600">Carregando...</span>
                </div>
            `;
        }
    }
    
    hideLoading(element, content = '') {
        if (typeof element === 'string') {
            element = document.getElementById(element);
        }
        
        if (element) {
            element.innerHTML = content;
        }
    }
    
    // Métodos de formulário
    validateForm(formElement) {
        const form = typeof formElement === 'string' ? document.getElementById(formElement) : formElement;
        if (!form) return false;
        
        const requiredFields = form.querySelectorAll('[required]');
        let isValid = true;
        
        requiredFields.forEach(field => {
            if (!field.value.trim()) {
                this.showFieldError(field, 'Campo obrigatório');
                isValid = false;
            } else {
                this.clearFieldError(field);
            }
        });
        
        return isValid;
    }
    
    showFieldError(field, message) {
        this.clearFieldError(field);
        
        const errorDiv = document.createElement('div');
        errorDiv.className = 'text-red-500 text-sm mt-1 field-error';
        errorDiv.textContent = message;
        
        field.parentElement.appendChild(errorDiv);
        field.classList.add('border-red-500');
    }
    
    clearFieldError(field) {
        const existingError = field.parentElement.querySelector('.field-error');
        if (existingError) {
            existingError.remove();
        }
        field.classList.remove('border-red-500');
    }
    
    // Métodos de logout
    async logout() {
        try {
            if (this.sessionId) {
                await this.api.post('/api/auth/logout');
            }
        } catch (error) {
            console.error('Erro ao fazer logout:', error);
        } finally {
            localStorage.clear();
            window.location.href = 'index.html';
        }
    }
    
    // Métodos de navegação
    navigateTo(page) {
        if (window.location.pathname !== `/${page}`) {
            window.location.href = page;
        }
    }
    
    // Métodos de utilidade para arrays
    groupBy(array, key) {
        return array.reduce((groups, item) => {
            const group = item[key];
            groups[group] = groups[group] || [];
            groups[group].push(item);
            return groups;
        }, {});
    }
    
    sortBy(array, key, order = 'asc') {
        return [...array].sort((a, b) => {
            const aVal = a[key];
            const bVal = b[key];
            
            if (order === 'asc') {
                return aVal > bVal ? 1 : -1;
            } else {
                return aVal < bVal ? 1 : -1;
            }
        });
    }
    
    filterBy(array, key, value) {
        return array.filter(item => item[key] === value);
    }
    
    search(array, searchTerm, keys) {
        const term = searchTerm.toLowerCase();
        return array.filter(item => 
            keys.some(key => 
                String(item[key]).toLowerCase().includes(term)
            )
        );
    }
}

// Instância global da aplicação
const app = new TechAlvesApp();

// Exportar para uso global
window.TechAlvesApp = TechAlvesApp;
window.app = app;

// Adicionar utilitários ao escopo global para compatibilidade
window.formatDate = app.formatDate.bind(app);
window.formatDateTime = app.formatDateTime.bind(app);
window.getStatusColor = app.getStatusColor.bind(app);
window.getStatusBadgeColor = app.getStatusBadgeColor.bind(app);
window.getStatusText = app.getStatusText.bind(app);
window.getPriorityBadgeColor = app.getPriorityBadgeColor.bind(app);
window.getPriorityText = app.getPriorityText.bind(app);
window.showNotification = app.showNotification.bind(app);
window.showLoading = app.showLoading.bind(app);
window.hideLoading = app.hideLoading.bind(app);
window.validateForm = app.validateForm.bind(app);
window.logout = app.logout.bind(app);