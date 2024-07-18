import { Link } from 'react-scroll';
import { Button, Typography } from '@mui/material';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import LocalActivityOutlinedIcon from '@mui/icons-material/LocalActivityOutlined';
import QueryStatsIcon from '@mui/icons-material/QueryStats';
import AdminUser from '../../Components/AdminUser/AdminUser.jsx';
import Reports from '../../Components/Reports/Reports.jsx';
import AdminEvents from '../../Components/AdminEvents/AdminEvents.jsx';
import '../Profile/profile.styles.css'
import './admin.styles.css';

const actions = [
    {
        id: 1,
        title: 'USUARIOS',
        componentID: 'admin-user',
        icon: <ManageAccountsIcon color="#cfc9d1" sx={{ width: 56, height: 56 }}/>,
    },
    {
        id: 2,
        title: 'EVENTOS',
        componentID: 'events-admin',
        icon: <LocalActivityOutlinedIcon color="#cfc9d1" sx={{ width: 56, height: 56 }} />,
    },
    {
        id: 3,
        title: 'REPORTES',
        componentID: 'reports',
        icon: <QueryStatsIcon color="#cfc9d1" sx={{ width: 56, height: 56 }} />,
    }  
]
const Admin = () => {
        return (
            <div>
                <div className='container-admin-header'>
                    <div className='card-action-container'>
                        {actions.map((action) => (
                            <Link to={action.componentID} spy={true} smooth={true} duration={500} offset={-320} key={action.id}>
                                <Button className="admin-card-actions">
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
                <h3 className='admin-divider' id='admin-user'>USUARIOS</h3>
                <AdminUser/>
                <h3 className='admin-divider' id="events-admin">EVENTOS</h3>
                <AdminEvents/>
                <h3 className='admin-divider' id='reports'>REPORTES</h3>
                <Reports/>
            </div>
    );
};
            
export default Admin