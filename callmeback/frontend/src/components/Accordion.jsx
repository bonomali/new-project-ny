import React, { Component } from 'react'
import { Accordion, Icon } from 'semantic-ui-react'

export default class AccordionExampleStandard extends Component {
  state = { activeIndex: -1 }

  handleClick = (e, titleProps) => {
    const { index } = titleProps
    const { activeIndex } = this.state
    const newIndex = activeIndex === index ? -1 : index

    this.setState({ activeIndex: newIndex })
  }

  render() {
    const { activeIndex } = this.state

    return (
      <Accordion>
        <Accordion.Title 
          active={activeIndex === 0}
          index={0}
          onClick={this.handleClick}
        >
          <Icon name='dropdown' />
          How do I file for unemployment?
        </Accordion.Title>
        <Accordion.Content active={activeIndex === 0}>
          <p>
          To file for unemployment, first determine your eligibility by visiting the  
          <a href="https://dol.ny.gov/unemployment/file-your-first-claim-benefits"> Department of Labor website. </a>
          If you are eligible, follow the instructions to file a claim.
          </p>
        </Accordion.Content>

        <Accordion.Title
          active={activeIndex === 1}
          index={1}
          onClick={this.handleClick}
        >
          <Icon name='dropdown' />
          If I am sick and cannot work, can I get unemployment?
        </Accordion.Title>
        <Accordion.Content active={activeIndex === 1}>
          <p> You can determine your eligibility by visiting the  
          <a href="https://dol.ny.gov/unemployment/file-your-first-claim-benefits"> Department of Labor website. </a>
          If you are eligible, follow the instructions to file a claim.
          </p>
        </Accordion.Content>

        <Accordion.Title
          active={activeIndex === 2}
          index={2}
          onClick={this.handleClick}
        >
          <Icon name='dropdown' />
          What happens if I am fired because of COVID-19?
        </Accordion.Title>
        <Accordion.Content active={activeIndex === 2}>
          <p> You may still be eligible for unemployment and can determine that at the  
          <a href="https://dol.ny.gov/unemployment/file-your-first-claim-benefits"> Department of Labor website. </a>
          If you are eligible, follow the instructions to file a claim.
          </p>
        </Accordion.Content>
      </Accordion>
    )
  }
}