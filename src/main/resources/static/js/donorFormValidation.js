document.addEventListener('DOMContentLoaded', function() {
    console.log("donorFormValidation.js loaded and executing!");
    const form = document.getElementById('registrationForm') || document.querySelector('form[th\\:action*="update"]'); // Get the form, either registration or update
    if (!form) {
        console.log("Form element not found. Exiting validation script.");
        return;
    }
    console.log("Form found:", form);

    const isRegistrationForm = form.id === 'registrationForm';
    console.log("Is Registration Form:", isRegistrationForm);

    const fields = [
        { id: 'fullName', validate: value => value.trim().length > 2, message: 'Full Name must be greater than 2 characters.' },
        { id: 'gender', validate: value => value !== '', message: 'Gender is required.' },
        { id: 'dateOfBirth', validate: value => value.trim() !== '' && new Date(value) < new Date(), message: 'Date of Birth is required and must be in the past.' },
        { id: 'bloodGroup', validate: value => value !== '', message: 'Blood Group is required.' },
        { id: 'phoneNumber', validate: value => /^[0-9]{10}$/.test(value), message: 'Phone Number must be exactly 10 digits.' },
        { id: 'email', validate: value => /^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,6}$/.test(value), message: 'Invalid email format.' },
        { id: 'occupation', validate: value => true, message: '' }, // Optional field
        { id: 'country', validate: value => true, message: '' }, // Optional field
        { id: 'state', validate: value => true, message: '' }, // Optional field
        { id: 'district', validate: value => true, message: '' }, // Optional field
        { id: 'city', validate: value => true, message: '' }, // Optional field
        { id: 'streetAddress', validate: value => true, message: '' }, // Optional field
        { id: 'zipCode', validate: value => true, message: '' }, // Optional field
        { id: 'landmark', validate: value => true, message: '' }, // Optional field
        { id: 'lastDonateDate', validate: value => true, message: '' }, // Optional field
    ];

    if (isRegistrationForm) {
        fields.push(
            { id: 'username', validate: value => value.trim() !== '', message: 'Username is required.' },
            { id: 'password', validate: value => value.length >= 8 && /[A-Z]/.test(value) && /[a-z]/.test(value) && /[0-9]/.test(value) && /[^A-Za-z0-9]/.test(value), message: 'Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.' }
        );
    } else {
        // For update form, password is optional, but if provided, validate strength
        fields.push(
            { id: 'username', validate: value => value.trim() !== '', message: 'Username is required.' },
            { id: 'password', validate: value => value === '' || (value.length >= 8 && /[A-Z]/.test(value) && /[a-z]/.test(value) && /[0-9]/.test(value) && /[^A-Za-z0-9]/.test(value)), message: 'Password must be at least 8 characters long and include uppercase, lowercase, number, and special character, or be empty.' }
        );
    }

    function validateField(field) {
        const input = document.getElementById(field.id);
        const errorSpan = document.getElementById(field.id + 'Error');
        console.log(`Validating field: ${field.id}, Input:`, input, `Error Span:`, errorSpan);

        if (!input) {
            console.warn(`Input element with ID ${field.id} not found.`);
            return true; // Cannot validate if input not found
        }
        if (!errorSpan) {
            console.warn(`Error span with ID ${field.id}Error not found.`);
            return true; // Cannot display error if span not found
        }

        const isValid = field.validate(input.value);
        console.log(`Field: ${field.id}, Value: "${input.value}", Is Valid: ${isValid}`);

        if (!isValid) {
            errorSpan.textContent = field.message;
            input.classList.add('border-red-500');
            input.classList.remove('border-gray-300');
            console.log(`Error displayed for ${field.id}: ${field.message}`);
        } else {
            errorSpan.textContent = '';
            input.classList.remove('border-red-500');
            input.classList.add('border-gray-300');
            console.log(`Validation passed for ${field.id}. Error cleared.`);
        }
        return isValid;
    }

    fields.forEach(field => {
        const input = document.getElementById(field.id);
        if (input) {
            console.log(`Attaching event listeners to ${field.id}`);
            input.addEventListener('input', () => validateField(field));
            input.addEventListener('blur', () => validateField(field)); // Validate on blur as well
        } else {
            console.warn(`Input element with ID ${field.id} not found when attaching listeners.`);
        }
    });

    form.addEventListener('submit', function(event) {
        console.log("Form submission attempted. Running final validation...");
        let allValid = true;
        fields.forEach(field => {
            if (!validateField(field)) {
                allValid = false;
            }
        });

        if (!allValid) {
            event.preventDefault(); // Prevent form submission if validation fails
            console.log("Form submission prevented due to validation errors.");
        } else {
            console.log("Form is valid. Submitting...");
        }
    });
});