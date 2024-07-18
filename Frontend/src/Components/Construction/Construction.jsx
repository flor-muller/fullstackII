import React from 'react'
import ConstructionIcon from '@mui/icons-material/Construction';
import './construction.styles.css'

const Construction = () => {
  return (
    <div className='in-construction-container'>
      <ConstructionIcon color="disabled" sx={{ width: 50, height: 50 }}/>
    </div>
  )
}

export default Construction