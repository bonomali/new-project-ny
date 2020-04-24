import React from 'react';
import { Card, Icon, List} from 'semantic-ui-react'

function Person(props) {
    return (
        <Card fluid className='run-card'>
          <Card.Content>
            <Card.Header>
              <Icon name='user' /> {props.person.name}
            </Card.Header>
            <Card.Description>
              <List size='tiny'>
                ID: {props.person.id}
              </List>
              <div className='text right'>
                Description: Hi, I'm {props.person.name}!
              </div>
            </Card.Description>
          </Card.Content>
        </Card>
    );
}

export default Person;