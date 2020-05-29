import React, { useState } from 'react';
import { Accordion, Icon } from 'semantic-ui-react';

function FaqAccordion() {
  const [activeIndex, setIndex] = useState(-1);

  const handleClick = (e, titleProps) => {
    const { index } = titleProps;
    const newIndex = activeIndex === index ? -1 : index;
    setIndex(newIndex);
  };

  return (
    <Accordion styled>
      <Accordion.Title
        active={activeIndex === 0}
        index={0}
        onClick={handleClick}
      >
        <Icon name='dropdown' />
        What changes are businesses expected to implement at their places of
        work?
      </Accordion.Title>
      <Accordion.Content active={activeIndex === 0}>
        <p>
          Non-essential businesses are to implement work-from-home policies, and
          essential businesses should check the
          <a href='https://esd.ny.gov/novel-coronavirus-faq-businesses'>
            {' '}
            latest updates.{' '}
          </a>
        </p>
      </Accordion.Content>

      <Accordion.Title
        active={activeIndex === 1}
        index={1}
        onClick={handleClick}
      >
        <Icon name='dropdown' />
        What financial assistance is available for my small business?
      </Accordion.Title>
      <Accordion.Content active={activeIndex === 1}>
        <p>
          {' '}
          Check out the
          <a href='http://www.nyssbdc.org/EIDL.html'>
            {' '}
            Small Business Financial Assistance Programs.{' '}
          </a>
          There you'll find information about the Paycheck Protection Program,
          loans, and debt relief.
        </p>
      </Accordion.Content>

      <Accordion.Title
        active={activeIndex === 2}
        index={2}
        onClick={handleClick}
      >
        <Icon name='dropdown' />
        How do I know if my business is designated as an essential service and
        allowed to open?
      </Accordion.Title>
      <Accordion.Content active={activeIndex === 2}>
        <p>
          {' '}
          See the New York State
          <a href='https://esd.ny.gov/guidance-executive-order-2026'>
            {' '}
            Guidance on Essential Businesses{' '}
          </a>
          for a comprehensive list of facilities and restrictions.
        </p>
      </Accordion.Content>
    </Accordion>
  );
}

export default FaqAccordion;
