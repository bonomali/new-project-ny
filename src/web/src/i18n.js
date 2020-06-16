import i18n from 'i18next';
import { initReactI18next } from "react-i18next";

// Relative imports outside of src/ aren't supported so hardcoding into
// "resources" for now below.
// import translationEN from '../public/locales/en/translation.json';
// import translationES from '../public/locales/es/translation.json';

// the translations
const resources = {
  en: {
    translation: "Hello World"
  },
  es: {
    translation: "Hola Mundo"
  },
};

i18n
  .use(initReactI18next) // passes i18n down to react-i18next
  .init({
    resources,
    lng: "es",

    keySeparator: false, // we do not use keys in form messages.welcome

    interpolation: {
      escapeValue: false // react already safes from xss
    }
  });

export default i18n;
