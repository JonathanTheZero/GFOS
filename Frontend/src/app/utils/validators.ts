import { AbstractControl } from '@angular/forms';

/**
 * custom validator in order to check wether the confirmation
 * equals the new password before sending it to the API 
 */
export function passwordMatchers(f: AbstractControl) {
    return f.get("newPassword").value === f.get("confirmNewPassword").value ?
        null : { passwordsNotEqual: true };
}