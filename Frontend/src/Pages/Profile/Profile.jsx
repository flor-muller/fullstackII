import React from 'react';
import { Link } from 'react-scroll';
import { Button, Typography } from '@mui/material';
import AccountCircleOutlinedIcon from '@mui/icons-material/AccountCircleOutlined';
import LocalActivityOutlinedIcon from '@mui/icons-material/LocalActivityOutlined';
import UserCard from '../../Components/UserCard/UserCard.jsx';
import UserTickets from '../../Components/UserTickets/UserTickets.jsx';
import './profile.styles.css';


const actions = [
    {
        id: 1,
        title: 'MIS DATOS',
        componentID: 'details',
        icon: <AccountCircleOutlinedIcon color="secondary" sx={{ width: 56, height: 56 }}/>,
    },
    {
        id: 2,
        title: 'MIS COMPRAS',
        componentID: 'tickets',
        icon: <LocalActivityOutlinedIcon color="secondary" sx={{ width: 56, height: 56 }} />,
    } 
]


const Profile = () => {
    return (
        <div>
            <div className='container-profile-header'>
                <div className='card-action-container'>
                    {actions.map((action) => (
                        <Link to={action.componentID} spy={true} smooth={true} duration={500} key={action.id}>
                            <Button className="card-actions">
                                    <div className='card-action-item'>
                                        <div>
                                            {action.icon}
                                        </div>
                                        <Typography variant="h7">
                                            {action.title}    
                                        </Typography>   
                                    </div>
                            </Button>
                        </Link>
                    ))}
                </div>
            </div>
            <div className='container-profile-body-first'>
                <h2 className='profile-divider'>MIS DATOS</h2>
                <UserCard/>
                <div className="wave">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320">
                        <path fill="#9c27b0" fill-opacity="1" d="M0,160L80,138.7C160,117,320,75,480,80C640,85,800,139,960,144C1120,149,1280,107,1360,85.3L1440,64L1440,320L1360,320C1280,320,1120,320,960,320C800,320,640,320,480,320C320,320,160,320,80,320L0,320Z"></path>
                    </svg>
                </div>
            </div>
            <div className='container-profile-body-second'>   
                <h2 className='profile-divider'>MIS COMPRAS</h2>
                <UserTickets/>
            </div>
        </div>
    );
};

export default Profile;
