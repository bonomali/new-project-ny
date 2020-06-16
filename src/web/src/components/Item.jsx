import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useTranslation } from 'react-i18next';
import i18n from '../i18n';

function Item(props) {
  const { id } = useParams();
  const [item, setItem] = useState();
  const changeLanguage = (lng) => {
    i18n.changeLanguage(lng);
  }
  const [t] = useTranslation();

  useEffect(() => {
    axios.get('/api/v1/items/' + id)
      .then((response) => {
        setItem(response.data);
      })
      .catch((reason) => {
        console.log(reason);
      });
  }, [id]);

  if (id === undefined || item === undefined) {
    return (
      <div data-testid='item-container'>
        Loading...
      </div>
    );
  }

  return (
    <div data-testid='item-container'>
      <p>Item {id} is named "{item.name}".</p>
      <button onClick={() => changeLanguage('es')}>es</button>
      <button onClick={() => changeLanguage('en')}>en</button>
      <h1>{t('Hello World')}</h1>
      <h1>{t('Item phrase', { id: '1', name: 'One' })}</h1>
    </div>
  );
}

export default Item;
