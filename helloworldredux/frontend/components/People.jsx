import React, { useState, useEffect } from 'react';

import { Card, Header, Container } from 'semantic-ui-react';

import Person from './Person.jsx';

function People(props) {

    const [anyPeople, setAnyPeople] = useState(true);
    useEffect(() => {
        setAnyPeople(!!props.people.length)
    }, [props.people]);

    const renderPeople = () =>
        props.people.map((person) => {
            return <Person key={person.id} person={person} />
        });

    return (
        <Container fluid style={{ padding: '1em 2em' }}>
            {!anyPeople && !props.people.length &&
            <div>
                <Header as="h2" textAlign="center">No people to display!</Header>
            </div>
            }
            {!!props.people.length &&
            <div>
                <Header as="h2" textAlign="center">Browse people:</Header>
                <Card.Group itemsPerRow="3">
                {renderPeople()}
                </Card.Group>
            </div>
            }
        </Container>
    )
}

export default People;