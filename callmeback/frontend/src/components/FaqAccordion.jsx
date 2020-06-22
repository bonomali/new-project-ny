/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React, {useState} from 'react';
import {Accordion, Icon} from 'semantic-ui-react';

/**
 * @return {string} Multiple accordions with sample FAQs
 */
function FaqAccordion() {
  const [activeIndex, setIndex] = useState(-1);

  const handleClick = (e, titleProps) => {
    const {index} = titleProps;
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
