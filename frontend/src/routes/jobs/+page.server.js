import axios from "axios";
import { error } from '@sveltejs/kit';
// Load environment variables from .env file for local development
import 'dotenv/config';
const API_BASE_URL = process.env.API_BASE_URL; // defined in frontend/.env

export async function load({ locals }) {
    const jwt_token = locals.jwt_token;
    const user_info = locals.user;

    if (!jwt_token) {
        return {
            jobs: [],
            companies: []
        };
    }

    try {
        // Could be done in parallel with Promise.all(...)
        const jobsResponse = await axios({
            method: "get",
            url: `${API_BASE_URL}/api/job`,
            headers: { Authorization: "Bearer " + jwt_token }
        });
        var companiesResponse = null;
        if (user_info.user_roles.includes('admin')) {
            companiesResponse = await axios({
                method: "get",
                url: `${API_BASE_URL}/api/company`,
                headers: { Authorization: "Bearer " + jwt_token }
            });
        } else {
            companiesResponse = { data: [] };
        }

        return {
            jobs: jobsResponse.data,
            companies: companiesResponse.data
        };

    } catch (axiosError) {
        console.log('Error loading companies:', axiosError);
    }
}

export const actions = {
    createJob: async ({ request, locals }) => {
        const jwt_token = locals.jwt_token;

        if (!jwt_token) {
            throw error(401, 'Authentication required');
        }

        const data = await request.formData();
        const job = {
            title: data.get('title'),
            description: data.get('description'),
            earnings: parseFloat(data.get('earnings')),
            jobType: data.get('jobType'),
            companyId: data.get('companyId')
        };

        try {
            await axios({
                method: "post",
                url: `${API_BASE_URL}/api/job`,
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + jwt_token
                },
                data: job,
            });

            return { success: true };
        } catch (error) {
            console.log('Error creating job:', error);
            return { success: false, error: 'Could not create job' };
        }
    }
}
