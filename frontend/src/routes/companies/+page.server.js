import axios from "axios";
import { error } from "@sveltejs/kit";
// Load environment variables from .env file for local development
import 'dotenv/config';
const API_BASE_URL = process.env.API_BASE_URL; // defined in frontend/.env

export async function load({ locals }) {
    const jwt_token = locals.jwt_token;

    if (!jwt_token) {
        return {
            companies: []
        };
    }

    try {
        const response = await axios({
            method: "get",
            url: `${API_BASE_URL}/api/company`,
            headers: { Authorization: "Bearer " + jwt_token },
        });

        return {
            companies: response.data
        };

    } catch (axiosError) {
        console.log('Error loading companies:', axiosError);
    }
}

export const actions = {
    createCompany: async ({ request, locals }) => {
        const jwt_token = locals.jwt_token;

        if (!jwt_token) {
            throw error(401, 'Authentication required');
        }

        const data = await request.formData();
        const company = {
            name: data.get('name'),
            email: data.get('email')
        };

        try {
            await axios({
                method: "post",
                url: `${API_BASE_URL}/api/company`,
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + jwt_token
                },
                data: company,
            });
            return { success: true };
        } catch (error) {
            console.log('Error creating company:', error);
            return { success: false, error: 'Could not create company' };
        }
    }
};
