import { computed, Injectable, signal } from "@angular/core";



@Injectable({ providedIn: 'root' })
export class UIStore {

    // Loader global
    isLoading = signal(false);

    // Messages globaux
    succesMessage = signal<string | null>(null);
    errorMessage = signal<string | null>(null);

    // Etats dérivés
    hasMessage = computed(() => this.succesMessage() !== null || this.errorMessage() !== null
    );

    // Méthodes loader
    startLoading() {
        this.isLoading.set(true);
    }

    stopLoading() {
        this.isLoading.set(false);
    }

    // Messages
    showSucces(msg: string) {
        this.succesMessage.set(msg),
        setTimeout(() => this.succesMessage.set(null), 2000);
    }

    showError(msg: string) {
        this.errorMessage.set(msg),
        setTimeout(() => this.errorMessage.set(null), 3000);
    }

    clearMessage() {
        this.succesMessage.set(null);
        this.errorMessage.set(null);
    }
    
}