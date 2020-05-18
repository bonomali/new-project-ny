import React from 'react'
import { useLocation } from "react-router";

function Header() {
  let location = useLocation();

  return (
    <div>
    {location.pathname==="/" || location.pathname==="/home" ?
      <header>
        <img src="/nygov-logo.png" alt="New York State logo" />
      </header> :
      <div className="corner-logo">
        <img src="/nygov-logo.png" alt="New York State logo" style={{"width":"5em"}}/>
      </div>}
    </div>
  );
}

export default Header;