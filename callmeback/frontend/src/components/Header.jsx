import React from 'react'
import { useLocation } from "react-router";

function Header() {
  let location = useLocation();
  console.log(location)
  return (
    <div>
    {location.pathname==="/" || location.pathname==="/home" ?
      <header>
        <img src="/nygov-logo.png" alt="New York State logo" width="30%" height="30%"/>
      </header> :
      <div className="corner-logo">
        <img src="/nygov-logo.png" alt="New York State logo" width="25%" height="25%"/>
      </div>}
    </div>
  );
}

export default Header;