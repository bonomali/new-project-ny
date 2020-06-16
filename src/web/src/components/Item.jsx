import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useTranslation } from 'react-i18next';
import i18n from '../i18n';
import { Dropdown } from 'semantic-ui-react'

function Item(props) {
  const { id } = useParams();
  const [item, setItem] = useState();

  const changeLanguage = (e, { value }) => {
    i18n.changeLanguage(value);
  }
  const [t] = useTranslation();

  const langOptions = [
    {
      key: 'en',
      text: 'English',
      value: 'en',
    },
    {
      key: 'es',
      text: 'Spanish',
      value: 'es',
    },
    {
      key: 'de',
      text: 'German',
      value: 'de',
    }
  ]

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
      <Dropdown
        onChange={changeLanguage}
        placeholder='Select Language'
        fluid
        selection
        options={langOptions}
      />
      <h1>{t('Hello World')}</h1>
      <h1>{t('Item phrase', { number: id, item: item })}</h1>
    </div>
  );
}

export default Item;
