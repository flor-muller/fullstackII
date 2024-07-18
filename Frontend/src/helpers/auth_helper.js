import { UserManager } from 'oidc-client';
import axios from 'axios';
import { keycloakHost, endpointHost } from '../variables/endpoints.js';

const settings = {
    authority: `${keycloakHost}/realms/ticketrek`,
    client_id: "frontend-ticketrek",
    redirect_uri: "http://localhost:5173/signin-callback.html",
    post_logout_redirect_uri: "http://localhost:5173/signout-callback.html",
    silent_redirect_uri: "http://localhost:5173/silent-renew.html",
    response_type: 'code',
    scope: "openid profile",
    automaticSilentRenew: true,
    filterProtocolClaims: true,
    loadUserInfo: true,
    issuer: `${keycloakHost}/realms/ticketrek`,
    authorization_endpoint: `${keycloakHost}/realms/ticketrek/protocol/openid-connect/auth`,
    userinfo_endpoint: `${keycloakHost}/realms/ticketrek/protocol/openid-connect/userinfo`,
    end_session_endpoint: `${keycloakHost}/realms/ticketrek/protocol/openid-connect/logout`,
    jwks_uri: `${keycloakHost}/realms/ticketrek/protocol/openid-connect/certs`
};

const userManager = new UserManager(settings);

export const getUser = () => {
    return userManager.getUser();
}

export const login = () => {
    return userManager.signinRedirect();
}

export const logout = () => {
    return userManager.signoutRedirect();
}

export const getUserRoles = async (userId, token) => {
    try {
        const response = await axios.get(`${endpointHost}/api/user/users/roles/${userId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        return response.data;
    } catch (error) {
        console.error("Error al obtener los roles del usuario", error);
        return [];
    }
}

userManager.events.addSilentRenewError(e => {
    console.error('Silent renew error', e.message);
});

userManager.events.addAccessTokenExpired(() => {
    console.log('Token expired, attempting silent renew...');
    userManager.signinSilent().catch(err => {
        console.error('Silent renew failed', err);
        userManager.signinRedirect();
    });
});

export { userManager };