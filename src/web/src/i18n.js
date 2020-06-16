import i18n from 'i18next';
import { initReactI18next } from "react-i18next";
import detector from "i18next-browser-languagedetector";

// Relative imports outside of src/ aren't supported so hardcoding into
// "resources" for now below.
// import translationEN from '../public/locales/en/translation.json';
// import translationES from '../public/locales/es/translation.json';

// the translations
const resources = {
  en: {
    translation: {
      "Hello World": "hello world"
    }
  },
  es: {
    translation: {
      "Hello World": "hola mundo"
    }
  },
};

i18n
  .use(detector)
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
