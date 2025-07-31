document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registrationForm') || document.getElementById('updateForm');
    if (!form) return;

    const isRegistrationForm = form.id === 'registrationForm';

    const fields = {
        'fullName': { validate: value => value.trim().length > 2, message: 'Full Name must be greater than 2 characters.' },
        'gender': { validate: value => value !== '', message: 'Gender is required.' },
        'dateOfBirth': { validate: value => value.trim() !== '' && new Date(value) < new Date(), message: 'Date of Birth is required and must be in the past.' },
        'bloodGroup': { validate: value => value !== '', message: 'Blood Group is required.' },
        'phoneNumber': { validate: value => /^[0-9]{10}$/.test(value), message: 'Phone Number must be exactly 10 digits.' },
        'email': { validate: value => /^(([^<>()[\\]\\.,;:\s@\"]+(\\.[^<>()[\\]\\.,;:\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\-0-9]+\\.)+[a-zA-Z]{2,}))$/.test(value), message: 'Invalid email format.' },
        'username': { validate: value => value.trim() !== '', message: 'Username is required.' },
        'password': {
            validate: value => {
                if (!isRegistrationForm && value === '') return true; // Optional on update
                return value.length >= 8 && /[A-Z]/.test(value) && /[a-z]/.test(value) && /[0-9]/.test(value) && /[^A-Za-z0-9]/.test(value);
            },
            message: 'Password must be 8+ characters with uppercase, lowercase, number, and special character.'
        },
        'country': { validate: value => value.trim().length > 0, message: 'Country is required.' },
        'state': { validate: value => value.trim().length > 0, message: 'State is required.' },
        'district': { validate: value => value.trim().length > 0, message: 'District is required.' },
        'city': { validate: value => value.trim().length > 0, message: 'City is required.' },
        'streetAddress': { validate: value => value.trim().length > 0, message: 'Street Address is required.' },
        'zipCode': { validate: value => /^[0-9]{5,6}$/.test(value), message: 'Zip Code must be 5 or 6 digits.' },
        'lastDonateDate': { validate: value => value.trim() !== '' && new Date(value) <= new Date(), message: 'Last Donate Date is required and cannot be in the future.' }
    };

    function validateField(input) {
        const fieldConfig = fields[input.id];
        if (!fieldConfig) return;

        const errorSpan = document.getElementById(input.id + 'Error');
        const isValid = fieldConfig.validate(input.value);

        errorSpan.textContent = isValid ? '' : fieldConfig.message;
        input.classList.toggle('border-red-500', !isValid);
        input.classList.toggle('border-gray-300', isValid);
    }

    form.addEventListener('input', event => {
        validateField(event.target);
    });

    form.addEventListener('change', event => {
        // For select and date inputs, 'change' event is more appropriate
        if (event.target.tagName === 'SELECT' || event.target.type === 'date') {
            validateField(event.target);
        }
    });

    form.addEventListener('submit', function(event) {
        let isFormValid = true;
        for (const fieldId in fields) {
            const input = document.getElementById(fieldId);
            if (input) {
                validateField(input);
                // Re-check validation result after validateField call
                if (!fields[fieldId].validate(input.value)) {
                    isFormValid = false;
                }
            }
        }

        if (!isFormValid) {
            event.preventDefault();
        }
    });
});