/* =========================================================
   LOANCONNECT — SCRIPT.JS
   Vanilla JavaScript only. No frameworks / dependencies.
   Sections:
   1. Utilities
   2. Navbar (sticky state, mobile menu, active link, smooth scroll)
   3. Scroll reveal animations
   4. Hero stat counters
   5. Scroll-to-top button
   6. EMI Calculator
   7. Loan application form (validation + localStorage + modal)
   8. Contact form (validation + toast)
   9. FAQ accordion
   10. Success modal controls
   ========================================================= */

document.addEventListener('DOMContentLoaded', () => {

  /* ---------------------------------------------------------
     1. UTILITIES
     --------------------------------------------------------- */

  // Format a number as Indian Rupee currency
  function formatCurrency(value) {
    if (isNaN(value)) return '₹0';
    return '₹' + Math.round(value).toLocaleString('en-IN');
  }

  // Show an inline field error message + invalid styling
  function setFieldError(inputEl, errorEl, message) {
    if (!inputEl || !errorEl) return;
    if (message) {
      inputEl.classList.add('is-invalid');
      errorEl.textContent = message;
      return false;
    } else {
      inputEl.classList.remove('is-invalid');
      errorEl.textContent = '';
      return true;
    }
  }

  /* ---------------------------------------------------------
     1b. DARK MODE TOGGLE
     --------------------------------------------------------- */

  const themeToggle = document.getElementById('themeToggle');
  const THEME_KEY = 'loanconnect-theme';

  // The <head> inline script already applied the saved theme (if any)
  // before first paint; here we just wire up the toggle button.
  function setTheme(theme) {
    if (theme === 'dark') {
      document.documentElement.setAttribute('data-theme', 'dark');
    } else {
      document.documentElement.removeAttribute('data-theme');
    }
    localStorage.setItem(THEME_KEY, theme);
    themeToggle.setAttribute('aria-pressed', theme === 'dark');
  }

  themeToggle.addEventListener('click', () => {
    const isDark = document.documentElement.getAttribute('data-theme') === 'dark';
    setTheme(isDark ? 'light' : 'dark');
  });

  // Reflect current state on the toggle for screen readers
  themeToggle.setAttribute('aria-pressed', document.documentElement.getAttribute('data-theme') === 'dark');

  /* ---------------------------------------------------------
     2. NAVBAR — sticky shadow, mobile menu, smooth scroll, active link
     --------------------------------------------------------- */

  const navbar = document.getElementById('navbar');
  const navToggle = document.getElementById('navToggle');
  const navMenu = document.getElementById('navMenu');
  const navLinks = document.querySelectorAll('.nav-link');

  // Add shadow to navbar once the page is scrolled
  window.addEventListener('scroll', () => {
    navbar.classList.toggle('is-scrolled', window.scrollY > 10);
    toggleScrollTopButton();
    updateActiveNavLink();
  });

  // Toggle hamburger menu on mobile
  navToggle.addEventListener('click', () => {
    navMenu.classList.toggle('is-open');
    navToggle.classList.toggle('is-open');
  });

  // Close mobile menu when a link is clicked
  navLinks.forEach(link => {
    link.addEventListener('click', () => {
      navMenu.classList.remove('is-open');
      navToggle.classList.remove('is-open');
    });
  });

  // Smooth scroll is handled natively via CSS `scroll-behavior: smooth`
  // combined with `scroll-padding-top` to offset for the sticky navbar.

  // Highlight the nav link matching the section currently in view
  const sections = document.querySelectorAll('section[id]');

  function updateActiveNavLink() {
    let currentId = '';
    const scrollPos = window.scrollY + 120;

    sections.forEach(section => {
      if (scrollPos >= section.offsetTop) {
        currentId = section.id;
      }
    });

    navLinks.forEach(link => {
      link.classList.toggle('active-link', link.getAttribute('href') === `#${currentId}`);
    });
  }

  /* ---------------------------------------------------------
     3. SCROLL REVEAL ANIMATIONS (IntersectionObserver)
     --------------------------------------------------------- */

  const revealEls = document.querySelectorAll('.reveal');

  const revealObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('is-visible');
        revealObserver.unobserve(entry.target);
      }
    });
  }, { threshold: 0.15 });

  revealEls.forEach(el => revealObserver.observe(el));

  /* ---------------------------------------------------------
     4. HERO STAT COUNTERS
     --------------------------------------------------------- */

  const statEls = document.querySelectorAll('.hero__stat-num');

  function animateCount(el) {
    const target = parseInt(el.dataset.count, 10) || 0;
    const duration = 1400;
    const start = performance.now();

    function tick(now) {
      const progress = Math.min((now - start) / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3); // ease-out cubic
      el.textContent = Math.round(eased * target).toLocaleString('en-IN');
      if (progress < 1) requestAnimationFrame(tick);
    }
    requestAnimationFrame(tick);
  }

  const statObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        animateCount(entry.target);
        statObserver.unobserve(entry.target);
      }
    });
  }, { threshold: 0.5 });

  statEls.forEach(el => statObserver.observe(el));

  /* ---------------------------------------------------------
     5. SCROLL-TO-TOP BUTTON
     --------------------------------------------------------- */

  const scrollTopBtn = document.getElementById('scrollTopBtn');

  function toggleScrollTopButton() {
    scrollTopBtn.classList.toggle('is-visible', window.scrollY > 500);
  }

  scrollTopBtn.addEventListener('click', () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  });

  /* ---------------------------------------------------------
     6. EMI CALCULATOR
     --------------------------------------------------------- */

  const emiForm = document.getElementById('emiForm');
  const emiAmountInput = document.getElementById('emiAmount');
  const emiRateInput = document.getElementById('emiRate');
  const emiTenureInput = document.getElementById('emiTenure');

  const emiMonthlyEl = document.getElementById('emiMonthly');
  const emiPrincipalEl = document.getElementById('emiPrincipal');
  const emiInterestEl = document.getElementById('emiInterest');
  const emiTotalEl = document.getElementById('emiTotal');
  const emiRingFg = document.getElementById('emiRingFg');

  const RING_CIRCUMFERENCE = 326.7; // 2 * PI * r(52), precomputed for the SVG ring

  emiForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const amount = parseFloat(emiAmountInput.value);
    const rate = parseFloat(emiRateInput.value);
    const years = parseFloat(emiTenureInput.value);

    // Validate: no empty / invalid fields allowed
    let valid = true;
    valid = setFieldError(emiAmountInput, document.getElementById('emiAmountError'),
      (!emiAmountInput.value || amount <= 0) ? 'Enter a valid loan amount' : '') && valid;

    valid = setFieldError(emiRateInput, document.getElementById('emiRateError'),
      (!emiRateInput.value || rate <= 0) ? 'Enter a valid interest rate' : '') && valid;

    valid = setFieldError(emiTenureInput, document.getElementById('emiTenureError'),
      (!emiTenureInput.value || years <= 0) ? 'Enter a valid tenure in years' : '') && valid;

    if (!valid) return;

    // EMI formula: EMI = P x R x (1+R)^N / ((1+R)^N - 1)
    // R = monthly interest rate, N = number of monthly instalments
    const monthlyRate = rate / 12 / 100;
    const months = years * 12;
    const emi = (amount * monthlyRate * Math.pow(1 + monthlyRate, months)) /
                (Math.pow(1 + monthlyRate, months) - 1);

    const totalPayable = emi * months;
    const totalInterest = totalPayable - amount;

    emiMonthlyEl.textContent = formatCurrency(emi);
    emiPrincipalEl.textContent = formatCurrency(amount);
    emiInterestEl.textContent = formatCurrency(totalInterest);
    emiTotalEl.textContent = formatCurrency(totalPayable);

    // Animate the progress ring to show principal-vs-interest proportion
    const principalShare = amount / totalPayable;
    const offset = RING_CIRCUMFERENCE * (1 - principalShare);
    emiRingFg.style.strokeDashoffset = offset;
  });

  // Clear error message as soon as the user starts correcting a field
  [emiAmountInput, emiRateInput, emiTenureInput].forEach(input => {
    input.addEventListener('input', () => {
      const errorEl = document.getElementById(input.id + 'Error');
      setFieldError(input, errorEl, '');
    });
  });

  /* ---------------------------------------------------------
     7. LOAN APPLICATION FORM
     --------------------------------------------------------- */

  const applyForm = document.getElementById('applyForm');

  // Pre-fill the loan type dropdown when a "Apply Now" button on a loan
  // card is clicked, then scroll to the application form.
  document.querySelectorAll('.loan-apply-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const loanType = btn.dataset.loan;
      const loanTypeSelect = document.getElementById('loanType');
      if (loanType && loanTypeSelect) {
        loanTypeSelect.value = loanType;
      }
    });
  });

  function validateApplyForm() {
    let valid = true;

    // Full Name
    const fullName = document.getElementById('fullName');
    valid = setFieldError(fullName, document.getElementById('fullNameError'),
      fullName.value.trim().length < 2 ? 'Please enter your full name' : '') && valid;

    // Mobile number — must be exactly 10 digits
    const mobile = document.getElementById('mobile');
    const mobilePattern = /^[0-9]{10}$/;
    valid = setFieldError(mobile, document.getElementById('mobileError'),
      !mobilePattern.test(mobile.value.trim()) ? 'Enter a valid 10-digit mobile number' : '') && valid;

    // Email
    const email = document.getElementById('email');
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    valid = setFieldError(email, document.getElementById('emailError'),
      !emailPattern.test(email.value.trim()) ? 'Enter a valid email address' : '') && valid;

    // Date of birth
    const dob = document.getElementById('dob');
    valid = setFieldError(dob, document.getElementById('dobError'),
      !dob.value ? 'Please select your date of birth' : '') && valid;

    // City
    const city = document.getElementById('city');
    valid = setFieldError(city, document.getElementById('cityError'),
      city.value.trim().length < 2 ? 'Please enter your city' : '') && valid;

    // Loan type
    const loanType = document.getElementById('loanType');
    valid = setFieldError(loanType, document.getElementById('loanTypeError'),
      !loanType.value ? 'Please select a loan type' : '') && valid;

    // Employment type
    const employmentType = document.getElementById('employmentType');
    valid = setFieldError(employmentType, document.getElementById('employmentTypeError'),
      !employmentType.value ? 'Please select employment type' : '') && valid;

    // Company / business name
    const companyName = document.getElementById('companyName');
    valid = setFieldError(companyName, document.getElementById('companyNameError'),
      companyName.value.trim().length < 2 ? 'Please enter company/business name' : '') && valid;

    // Monthly income — must be greater than zero
    const monthlyIncome = document.getElementById('monthlyIncome');
    valid = setFieldError(monthlyIncome, document.getElementById('monthlyIncomeError'),
      (!monthlyIncome.value || parseFloat(monthlyIncome.value) <= 0) ? 'Enter a valid monthly income' : '') && valid;

    // Loan amount — must be greater than zero
    const loanAmount = document.getElementById('loanAmount');
    valid = setFieldError(loanAmount, document.getElementById('loanAmountError'),
      (!loanAmount.value || parseFloat(loanAmount.value) <= 0) ? 'Enter a valid loan amount' : '') && valid;

    // Loan tenure
    const loanTenure = document.getElementById('loanTenure');
    valid = setFieldError(loanTenure, document.getElementById('loanTenureError'),
      (!loanTenure.value || parseFloat(loanTenure.value) <= 0) ? 'Enter a valid loan tenure' : '') && valid;

    // Purpose of loan
    const loanPurpose = document.getElementById('loanPurpose');
    valid = setFieldError(loanPurpose, document.getElementById('loanPurposeError'),
      loanPurpose.value.trim().length < 5 ? 'Please describe the purpose of the loan' : '') && valid;

    // Aadhaar & PAN uploads are required, salary slip is optional
    const aadhaar = document.getElementById('aadhaar');
    valid = setFieldError(aadhaar, document.getElementById('aadhaarError'),
      !aadhaar.files.length ? 'Please upload your Aadhaar card' : '') && valid;

    const pan = document.getElementById('pan');
    valid = setFieldError(pan, document.getElementById('panError'),
      !pan.files.length ? 'Please upload your PAN card' : '') && valid;

    // Terms & conditions checkbox
    const terms = document.getElementById('terms');
    const termsError = document.getElementById('termsError');
    if (!terms.checked) {
      termsError.textContent = 'You must agree to the Terms & Conditions';
      valid = false;
    } else {
      termsError.textContent = '';
    }

    return valid;
  }

  // Live-clear errors as the user edits each field
  applyForm.querySelectorAll('input, select, textarea').forEach(field => {
    const evt = (field.type === 'checkbox' || field.type === 'file') ? 'change' : 'input';
    field.addEventListener(evt, () => {
      const errorEl = document.getElementById(field.id + 'Error');
      if (errorEl) setFieldError(field, errorEl, '');
    });
  });

  applyForm.addEventListener('submit', (e) => {
    e.preventDefault(); // Prevent page refresh

    if (!validateApplyForm()) {
      // Scroll to the first invalid field for convenience
      const firstInvalid = applyForm.querySelector('.is-invalid');
      if (firstInvalid) firstInvalid.scrollIntoView({ behavior: 'smooth', block: 'center' });
      return;
    }

    // Collect form data. File inputs only store the file name — actual
    // file bytes are not persisted to localStorage.
    const applicationData = {
      id: 'APP-' + Date.now(),
      submittedAt: new Date().toISOString(),
      fullName: document.getElementById('fullName').value.trim(),
      mobile: document.getElementById('mobile').value.trim(),
      email: document.getElementById('email').value.trim(),
      dob: document.getElementById('dob').value,
      city: document.getElementById('city').value.trim(),
      loanType: document.getElementById('loanType').value,
      employmentType: document.getElementById('employmentType').value,
      companyName: document.getElementById('companyName').value.trim(),
      monthlyIncome: document.getElementById('monthlyIncome').value,
      loanAmount: document.getElementById('loanAmount').value,
      loanTenure: document.getElementById('loanTenure').value,
      loanPurpose: document.getElementById('loanPurpose').value.trim(),
      documents: {
        aadhaar: document.getElementById('aadhaar').files[0]?.name || '',
        pan: document.getElementById('pan').files[0]?.name || '',
        salarySlip: document.getElementById('salarySlip').files[0]?.name || ''
      }
    };

    fetch("http://localhost:8080/api/loan-applications", {
      method: "POST",
      headers: {
          "Content-Type": "application/json"
      },
      body: JSON.stringify(applicationData)
  })
  .then(async (response) => {
      if (!response.ok) {
          throw new Error(await response.text());
      }
  
      return response.json();
  })
  .then((data) => {
      console.log("Backend Response:", data);
  
      openModal("Your loan application has been submitted successfully.");
      applyForm.reset();
  })
  .catch((error) => {
      console.error(error);
      openModal("Application submission failed.");
  });
    applyForm.reset();
  });

  // Store submitted applications as an array in localStorage
  function saveApplicationToLocalStorage(data) {
    try {
      const existing = JSON.parse(localStorage.getItem('loanApplications')) || [];
      existing.push(data);
      localStorage.setItem('loanApplications', JSON.stringify(existing));
    } catch (err) {
      console.error('Could not save application to localStorage:', err);
    }
  }

  /* ---------------------------------------------------------
     8. CONTACT FORM
     --------------------------------------------------------- */

  const contactForm = document.getElementById('contactForm');
  const toast = document.getElementById('toast');
  let toastTimer = null;

  function showToast(message) {
    toast.textContent = message;
    toast.classList.add('is-visible');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => toast.classList.remove('is-visible'), 3200);
  }

  contactForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const name = document.getElementById('contactName');
    const email = document.getElementById('contactEmail');
    const message = document.getElementById('contactMessage');
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    let valid = true;
    valid = setFieldError(name, document.getElementById('contactNameError'),
      name.value.trim().length < 2 ? 'Please enter your name' : '') && valid;
    valid = setFieldError(email, document.getElementById('contactEmailError'),
      !emailPattern.test(email.value.trim()) ? 'Enter a valid email address' : '') && valid;
    valid = setFieldError(message, document.getElementById('contactMessageError'),
      message.value.trim().length < 5 ? 'Please enter a message' : '') && valid;

    if (!valid) return;

    showToast('Message sent! We will get back to you shortly.');
    contactForm.reset();
  });

  contactForm.querySelectorAll('input, textarea').forEach(field => {
    field.addEventListener('input', () => {
      const errorEl = document.getElementById(field.id + 'Error');
      if (errorEl) setFieldError(field, errorEl, '');
    });
  });

  /* ---------------------------------------------------------
     9. FAQ ACCORDION
     --------------------------------------------------------- */

  const accordionItems = document.querySelectorAll('.accordion__item');

  accordionItems.forEach(item => {
    const trigger = item.querySelector('.accordion__trigger');
    const panel = item.querySelector('.accordion__panel');

    trigger.addEventListener('click', () => {
      const isOpen = item.classList.contains('is-open');

      // Close all other items (single-open accordion behaviour)
      accordionItems.forEach(other => {
        other.classList.remove('is-open');
        other.querySelector('.accordion__panel').style.maxHeight = null;
      });

      if (!isOpen) {
        item.classList.add('is-open');
        panel.style.maxHeight = panel.scrollHeight + 'px';
      }
    });
  });

  /* ---------------------------------------------------------
     10. SUCCESS MODAL
     --------------------------------------------------------- */

  const successModal = document.getElementById('successModal');
  const modalMessage = document.getElementById('modalMessage');
  const modalClose = document.getElementById('modalClose');
  const modalOkBtn = document.getElementById('modalOkBtn');

  function openModal(message) {
    modalMessage.textContent = message;
    successModal.classList.add('is-visible');
    document.body.style.overflow = 'hidden';
  }

  function closeModal() {
    successModal.classList.remove('is-visible');
    document.body.style.overflow = '';
  }

  modalClose.addEventListener('click', closeModal);
  modalOkBtn.addEventListener('click', closeModal);
  successModal.addEventListener('click', (e) => {
    if (e.target === successModal) closeModal();
  });
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && successModal.classList.contains('is-visible')) closeModal();
  });

  /* ---------------------------------------------------------
     FOOTER YEAR
     --------------------------------------------------------- */
  document.getElementById('year').textContent = new Date().getFullYear();

  // Initialise state on load
  toggleScrollTopButton();
  updateActiveNavLink();
});
