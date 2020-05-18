import React from 'react'
import Link from 'next/link'
import { Menu } from 'semantic-ui-react'

function Layout() {
    return (
      <div>
        <Menu color='grey' size='large' inverted secondary className='nav-bar'>
          <Menu.Item>
            <Link href='/'>
              <a>People App</a>
            </Link>
          </Menu.Item>
          <Menu.Item>
              <a>Login or Sign Up</a>
          </Menu.Item>
        </Menu>
      </div>
    )
}

export default Layout;
