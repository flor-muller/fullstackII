import axios from 'axios';
import { getUser, userManager } from './auth_helper';
import { endpointHost } from '../variables/endpoints.js';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';


const getAuthHeaders = async () => {
    let user = await getUser();
    if (user && user.expired) {
        console.log('Token expired, attempting silent renew...');
        user = await userManager.signinSilent();
    }
    if (user && user.access_token) {
        return {
            Accept: "application/json",
            Authorization: "Bearer " + user.access_token
        };
    } else {
        throw new Error('Debes iniciar sesión');
    }
};

//UserCard GET
export const getUserInfo = async () => {
    const headers = await getAuthHeaders();
    return axios.get(`${endpointHost}/api/user/users/info`, { headers });
};

// ProfileEdit PUT
export const updateUserInfo = async (userData) => {
    const headers = await getAuthHeaders();
    return axios.put(`${endpointHost}/api/user/users/update`, userData, { headers });
};

// UserPurchases GET
export const getUserPurchases = async (idUser) => {
    const headers = await getAuthHeaders();
    return axios.get(`${endpointHost}/api/ticket/tickets/findByUserId/${idUser}`, { headers })
}

export const getAllEvents = async () => {
    try {
        const response = await axios.get(`${endpointHost}/api/events/event/public/get/all`);
        return response.data; 
    } catch (error) {
        console.error('Error fetching events:', error);
        throw error;
    }
};


export const getAllCategories = async () => {
    try {
        const response = await axios.get(`${endpointHost}/api/events/category/public/get/all`);
        return response.data; 
    } catch (error) {
        console.error('Error fetching cities:', error);
        throw error;
    }
};

export const getAllCities = async () => {
    try {
        const response = await axios.get(`${endpointHost}/api/place/city/public/all`);
        return response.data.map(city => ({ id: city.id, name: city.name }));
    } catch (error) {
        console.error('Error fetching categories:', error);
        throw error;
    }
};

export const searchEvents = async (name, city, category, date) => {
    try {
        //dayjs.extend(utc);
        let baseUrl = `${endpointHost}/api/events/event/public/search?`
        const params = [];
        if (name) {
            params.push(`name=${name}`);
        }
        if (city) {
            params.push(`city=${city}`);
        }
        if (category) {
            params.push(`category=${category}`);
        }

        if (params.length > 0) {
            baseUrl += params.join("&");
        }
        console.log('searching events')
        const response = await axios.get(baseUrl);
        let events = response.data;
        if (date) {
            console.log('filtering by date')
            const filterDateArray = (date.split("T")[0]).split("-")
            events = events.filter(event=>{
                const eventDateArray = (event.dateEvent.date.split("T")[0]).split("-");
                const result = sameDate(eventDateArray, filterDateArray);
                console.log(result);
                return result;
            })
        }
        return events;
    }
    catch (e) {
        console.error(e);
        throw e;
    }
}

const sameDate = (eventDate, filterDate) => {
    console.log(eventDate);
    console.log(filterDate);
    if ((eventDate[0] === filterDate[0]) && (eventDate[1] === filterDate[1]) && (eventDate[2] === filterDate[2])){
        return true;
    }
    else {
        return false
    }
}

export const getEventById = async (id) => {
    try {
        const response = await axios.get(`${endpointHost}/api/events/event/public/getById/${id}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching event by id:', error);
        throw error;
    }
};

export const getEventPlace = async (placeId) => {
    try {
        const response = await axios.get(`${endpointHost}/api/place/place/public/id/${placeId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching event place:', error);
        throw error;
    }
};

export const getPaymentMethods = async () => {
    try {
        const response = await axios.get(`${endpointHost}/api/ticket/paymentMethod/public/all`);
        return response.data;
    } catch (error) {
        console.error('Error fetching payment methods:', error);
        throw error;
    }
};

export const getSeatsIds = async (zoneId) => {
    try {
        const response = await axios.get(`${endpointHost}/api/place/seat/public/zone/availability/${zoneId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching seats:', error);
        throw error;
    }
};

export const getAllPlaces = async () => {
    try {
        const response = await axios.get(`${endpointHost}/api/place/place/public/all`);
        return response.data;
    } catch (error) {
        console.error('Error fetching places:', error);
        throw error;
    }
};

export const createTicket = async (zoneQuantity, paymentMethod, userToken, eventId) => {
    try {
        let seatsIds = [];
        const seatsPromises = zoneQuantity.map(async (zone) => {
            const availableSeats = await getSeatsIds(zone.id);
            if (!availableSeats || availableSeats.length < zone.quantity) {
                return false;
            }
            return availableSeats.slice(0, zone.quantity).map(seat => seat.id);
        });
        const allSeats = await Promise.all(seatsPromises);
        const validSeats = allSeats.filter(seatGroup => seatGroup !== false);
        seatsIds = validSeats.flat();
        const headers = await getAuthHeaders();
        const data = {
            userId: userToken,
            paymentMethodId: paymentMethod,
            seatsId: seatsIds,
            eventId: eventId
        };
        console.log(`create ticket request data: ${JSON.stringify(data, null, 2)}`)
        console.log(`create ticket request headers: ${JSON.stringify(headers, null, 2)}`)
        const response = await axios.post(
            `${endpointHost}/api/ticket/tickets/create`, data, { headers });
        return response.data;
    } catch (error) {
        console.error('Error creating ticket:', error);
        throw error;
    }
}

// export const getAuthHeaders = async () => {
//     const eventsResponse = await fetch(`${endpointHost}/api/events/event/public/get/all`);
// }
// const eventsResponse = await fetch(`${endpointHost}/api/events/event/public/get/all`);
//         const events = await eventsResponse.json();

export const getAllUsersFromKeycloak = async () => {
    const headers = await getAuthHeaders();
    return axios.get(`${endpointHost}/api/user/users/all`, { headers });
}

export const deleteUserById = async (idUser) => {
    const headers = await getAuthHeaders();
    return axios.delete(`${endpointHost}/api/user/users/delete/${idUser}`, { headers })
}

//UserList DELETE
export const deleteUserRole = async (id, role) => {
    const headers = await getAuthHeaders();
    return axios.delete(`${endpointHost}/api/user/users/roles/deleteRole`, {
        headers,
        params: {
            id: id,
            role: role
        }
    });
}

//UserList PUT
export const addUserRole = async (id, role) => {
    const headers = await getAuthHeaders();
    return axios.put(
        `${endpointHost}/api/user/users/roles/addRole`,
        null,
        {
            headers,
            params: {
                id: id,
                role: role
            }
        }
    );
}

