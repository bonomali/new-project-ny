import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useTranslation } from 'react-i18next';

function App() {
  const [t] = useTranslation();
  return <h1>{t('Hello World')}</h1>
}

function Item(props) {
  const { id } = useParams();
  const [item, setItem] = useState();

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
    </div>
  );
}

export default App; //,Item
