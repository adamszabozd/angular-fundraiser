import {FormGroup} from "@angular/forms";

export function mustMatch(password: string, confirmPassword: string) {
    return (formGroup: FormGroup) => {
        const control = formGroup.controls[password];
        const matchingControl = formGroup.controls[confirmPassword];

        if (matchingControl.errors && !matchingControl.errors.mustMatch) {
            return;
        }
        if (control.value !== matchingControl.value) {
            matchingControl.setErrors({mustMatch: true});
        } else {
            matchingControl.setErrors(null);
        }
    };
}

export function minAmount(amount: string) {
    return (formGroup: FormGroup) => {
        const control = formGroup.controls[amount];

        if (control.errors && !control.errors.minAmount) {
            return;
        }
        if (control.value <= 0) {
            control.setErrors({minAmount: true});
        } else {
            control.setErrors(null);
        }
    }
}
