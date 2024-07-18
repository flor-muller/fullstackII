import React from 'react'
import { RingLoader } from "react-spinners";
import "./spinner.styles.css";

const Spinner = () => {
  return (
    <div className="spinner-loading-div">
        <RingLoader
            color="#4e45bc"
            loading
            size={80}
            speedMultiplier={1.2}
            />
    </div>
  )
}

export default Spinner