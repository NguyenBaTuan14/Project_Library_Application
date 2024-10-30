export const oktaConfig = {
    clientId: '0oaiqr9dit152i09r5d7',
    issuer: 'https://dev-57303335.okta.com/oauth2/default',
    redirectUri: 'http://localhost:3000/login/callback',
    scopes: ['openid', 'profile', 'email'],
    pkce: true,
    disableHttpsCheck: true,
}