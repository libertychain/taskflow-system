// TechAlves Soluções - Main JavaScript File
// Funcionalidades compartilhadas entre as páginas

function TechAlvesApp() {
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

TechAlvesApp.prototype.init = function() {
    this.setupInterceptors();
    this.checkAuthentication();
};

TechAlvesApp.prototype.setupInterceptors = function() {
    var self = this;
    // Adicionar token de autenticação a todas as requisições
    this.api.interceptors.request.use(function(config) {
        if (self.sessionId) {
            config.headers.Authorization = 'Bearer ' + self.sessionId;
        }
        return config;
    });
    
    // Tratar respostas de erro
    this.api.interceptors.response.use(
        function(response) { return response; },
        function(error) {
            if (error.response && error.response.status === 401) {
                self.handleUnauthorized();
            }
            return Promise.reject(error);
        }
    );
};

TechAlvesApp.prototype.checkAuthentication = function() {
    if (!this.sessionId) {
        this.redirectToLogin();
        return;
    }
    
    var self = this;
    this.api.get('/api/auth/validate')
        .then(function(response) {
            if (!response.data.valid) {
                self.handleUnauthorized();
            }
        })
        .catch(function(error) {
            self.handleUnauthorized();
        });
};

TechAlvesApp.prototype.handleUnauthorized = function() {
    localStorage.clear();
    this.redirectToLogin();
};

TechAlvesApp.prototype.redirectToLogin = function() {
    if (window.location.pathname !== '/index.html' && window.location.pathname !== '/') {
        window.location.href = 'index.html';
    }
};

// Métodos utilitários
TechAlvesApp.prototype.formatDate = function(dateString) {
    if (!dateString) return 'Não definido';
    return new Date(dateString).toLocaleDateString('pt-BR');
};

TechAlvesApp.prototype.formatDateTime = function(dateString) {
    if (!dateString) return 'Não definido';
    return new Date(dateString).toLocaleString('pt-BR');
};

TechAlvesApp.prototype.getStatusColor = function(status) {
    var colors = {
        'PENDING': 'bg-yellow-400',
        'IN_PROGRESS': 'bg-blue-400',
        'COMPLETED': 'bg-green-400',
        'CANCELLED': 'bg-red-400',
        'ACTIVE': 'bg-blue-400',
        'ON_HOLD': 'bg-yellow-400'
    };
    return colors[status] || 'bg-gray-400';
};

TechAlvesApp.prototype.getStatusBadgeColor = function(status) {
    var colors = {
        'PENDING': 'bg-yellow-100 text-yellow-800',
        'IN_PROGRESS': 'bg-blue-100 text-blue-800',
        'COMPLETED': 'bg-green-100 text-green-800',
        'CANCELLED': 'bg-red-100 text-red-800',
        'ACTIVE': 'bg-blue-100 text-blue-800',
        'ON_HOLD': 'bg-yellow-100 text-yellow-800'
    };
    return colors[status] || 'bg-gray-100 text-gray-800';
};

TechAlvesApp.prototype.getStatusText = function(status) {
    var texts = {
        'PENDING': 'Pendente',
        'IN_PROGRESS': 'Em Progresso',
        'COMPLETED': 'Concluída',
        'CANCELLED': 'Cancelada',
        'ACTIVE': 'Ativo',
        'ON_HOLD': 'Em Espera'
    };
    return texts[status] || status;
};

TechAlvesApp.prototype.getPriorityBadgeColor = function(priority) {
    var colors = {
        'LOW': 'bg-green-100 text-green-800',
        'MEDIUM': 'bg-yellow-100 text-yellow-800',
        'HIGH': 'bg-red-100 text-red-800',
        'URGENT': 'bg-red-200 text-red-900'
    };
    return colors[priority] || 'bg-gray-100 text-gray-800';
};

TechAlvesApp.prototype.getPriorityText = function(priority) {
    var texts = {
        'LOW': 'Baixa',
        'MEDIUM': 'Média',
        'HIGH': 'Alta',
        'URGENT': 'Urgente'
    };
    return texts[priority] || priority;
};

// Métodos de notificação
TechAlvesApp.prototype.showNotification = function(message, type) {
    type = type || 'info';
    // Criar elemento de notificação
    var notification = document.createElement('div');
    notification.className = 'fixed top-4 right-4 p-4 rounded-lg shadow-lg z-50 ' + this.getNotificationClass(type);
    notification.innerHTML =
        '<div class="flex items-center">' +
            '<i class="fas ' + this.getNotificationIcon(type) + ' mr-2"></i>' +
            '<span>' + message + '</span>' +
            '<button onclick="this.parentElement.parentElement.remove()" class="ml-4 text-gray-500 hover:text-gray-700">' +
                '<i class="fas fa-times"></i>' +
            '</button>' +
        '</div>';
    
    document.body.appendChild(notification);
    
    // Remover após 5 segundos
    setTimeout(function() {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
};

TechAlvesApp.prototype.getNotificationClass = function(type) {
    var classes = {
        'success': 'bg-green-500 text-white',
        'error': 'bg-red-500 text-white',
        'warning': 'bg-yellow-500 text-white',
        'info': 'bg-blue-500 text-white'
    };
    return classes[type] || classes.info;
};

TechAlvesApp.prototype.getNotificationIcon = function(type) {
    var icons = {
        'success': 'fa-check-circle',
        'error': 'fa-exclamation-circle',
        'warning': 'fa-exclamation-triangle',
        'info': 'fa-info-circle'
    };
    return icons[type] || icons.info;
};

// Métodos de loading
TechAlvesApp.prototype.showLoading = function(element) {
    if (typeof element === 'string') {
        element = document.getElementById(element);
    }
    
    if (element) {
        element.innerHTML = 
            '<div class="flex items-center justify-center py-8">' +
                '<i class="fas fa-spinner fa-spin text-2xl text-blue-500 mr-2"></i>' +
                '<span class="text-gray-600">Carregando...</span>' +
            '</div>';
    }
};

TechAlvesApp.prototype.hideLoading = function(element, content) {
    content = content || '';
    if (typeof element === 'string') {
        element = document.getElementById(element);
    }
    
    if (element) {
        element.innerHTML = content;
    }
};

// Métodos de formulário
TechAlvesApp.prototype.validateForm = function(formElement) {
    var form = typeof formElement === 'string' ? document.getElementById(formElement) : formElement;
    if (!form) return false;
    
    var requiredFields = form.querySelectorAll('[required]');
    var isValid = true;
    
    var self = this;
    Array.prototype.forEach.call(requiredFields, function(field) {
        if (!field.value.trim()) {
            self.showFieldError(field, 'Campo obrigatório');
            isValid = false;
        } else {
            self.clearFieldError(field);
        }
    });
    
    return isValid;
};

TechAlvesApp.prototype.showFieldError = function(field, message) {
    this.clearFieldError(field);
    
    var errorDiv = document.createElement('div');
    errorDiv.className = 'text-red-500 text-sm mt-1 field-error';
    errorDiv.textContent = message;
    
    field.parentElement.appendChild(errorDiv);
    field.classList.add('border-red-500');
};

TechAlvesApp.prototype.clearFieldError = function(field) {
    var existingError = field.parentElement.querySelector('.field-error');
    if (existingError) {
        existingError.remove();
    }
    field.classList.remove('border-red-500');
};

// Métodos de logout
TechAlvesApp.prototype.logout = function() {
    var logoutAndRedirect = function() {
        localStorage.clear();
        window.location.href = 'index.html';
    };

    if (this.sessionId) {
        this.api.post('/api/auth/logout')
            .then(logoutAndRedirect)
            .catch(function(error) {
                console.error('Erro ao fazer logout:', error);
                logoutAndRedirect();
            });
    } else {
        logoutAndRedirect();
    }
};

// Métodos de navegação
TechAlvesApp.prototype.navigateTo = function(page) {
    if (window.location.pathname !== '/' + page) {
        window.location.href = page;
    }
};

// Métodos de utilidade para arrays
TechAlvesApp.prototype.groupBy = function(array, key) {
    return array.reduce(function(groups, item) {
        var group = item[key];
        groups[group] = groups[group] || [];
        groups[group].push(item);
        return groups;
    }, {});
};

TechAlvesApp.prototype.sortBy = function(array, key, order) {
    order = order || 'asc';
    return array.slice().sort(function(a, b) {
        var aVal = a[key];
        var bVal = b[key];
        
        if (order === 'asc') {
            return aVal > bVal ? 1 : -1;
        } else {
            return aVal < bVal ? 1 : -1;
        }
    });
};

TechAlvesApp.prototype.filterBy = function(array, key, value) {
    return array.filter(function(item) { return item[key] === value; });
};

TechAlvesApp.prototype.search = function(array, searchTerm, keys) {
    var term = searchTerm.toLowerCase();
    return array.filter(function(item) {
        return keys.some(function(key) {
            return String(item[key]).toLowerCase().includes(term);
        });
    });
};

// Instância global da aplicação
var app = new TechAlvesApp();

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
