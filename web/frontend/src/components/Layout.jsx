import React from 'react'
import { NavLink } from 'react-router-dom'
import { Menu } from 'semantic-ui-react'

function Layout() {
    return (
      <div>
        <Menu color='grey' size='large' inverted secondary className='nav-bar'>
          <Menu.Item>
            <NavLink to='/home'>People App</NavLink>
          </Menu.Item>
          <Menu.Item>
              <a>Login or Sign Up</a>
          </Menu.Item>
        </Menu>
      </div>
    )
}

export default Layout;