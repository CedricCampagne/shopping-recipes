import { computed, Injectable, signal } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class UIStore {

    // Loader global
    isLoading = signal(false);

    // Messages globaux
    successMessage = signal<string | null>(null);
    errorMessage = signal<string | null>(null);

    // Etats dérivés
    hasMessage = computed(() => this.successMessage() !== null || this.errorMessage() !== null
    );

    // Méthodes loader
    startLoading() {
        this.isLoading.set(true);
    }

    stopLoading() {
        this.isLoading.set(false);
    }

    // Messages
    showSuccess(msg: string) {
        this.successMessage.set(msg),
        setTimeout(() => this.successMessage.set(null), 2000);
    }

    showError(msg: string) {
        this.errorMessage.set(msg),
        setTimeout(() => this.errorMessage.set(null), 3000);
    }

    clearMessage() {
        this.successMessage.set(null);
        this.errorMessage.set(null);
    }

}