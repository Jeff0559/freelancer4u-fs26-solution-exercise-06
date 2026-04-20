import axios from "axios";
import { error } from '@sveltejs/kit';
// Load environment variables from .env file for local development
import 'dotenv/config';
const API_BASE_URL = process.env.API_BASE_URL; // defined in frontend/.env

export async function load({ url, locals }) {
    const jwt_token = locals.jwt_token;
    const user_info = locals.user;

    if (!jwt_token) {
        return {
            jobs: [],
            companies: [],
            nrOfPages: 0,
            currentPage: 1
        };
    }

    try {
        // Get URL parameters for pagination
        const currentPage = parseInt(url.searchParams.get('pageNumber') || '1');
        const pageSize = parseInt(url.searchParams.get('pageSize') || '4');

        // Build query string
        let query = `?pageSize=${pageSize}&pageNumber=${currentPage}`;

        // Could be done in parallel with Promise.all(...)
        const jobsResponse = await axios({
            method: "get",
            url: `${API_BASE_URL}/api/job` + query,
            headers: { Authorization: "Bearer " + jwt_token }
        });

        var companiesResponse = null;
        if (user_info.user_roles.includes('admin')) {
            companiesResponse = await axios({
                method: "get",
                url: `${API_BASE_URL}/api/company?pageNumber=1&pageSize=1000`,
                headers: { Authorization: "Bearer " + jwt_token }
            });
        } else {
            companiesResponse = { data: { content: [] } };
        }

        return {
            jobs: jobsResponse.data.content,
            companies: companiesResponse.data.content,
            nrOfPages: jobsResponse.data.totalPages || 0,
            currentPage: currentPage
        };

    } catch (axiosError) {
        console.log('Error loading jobs:', axiosError);
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
        } catch (err) {
            console.log('Error creating job:', err);
            return { success: false, error: 'Could not create job' };
        }
    },

    assignToMe: async ({ request, locals }) => {
        const jwt_token = locals.jwt_token;
        if (!jwt_token) {
            throw error(401, 'Authentication required');
        }
        const data = await request.formData();
        const jobId = data.get('jobId');
        try {
            await axios({
                method: "put",
                url: `${API_BASE_URL}/api/service/me/assignjob?jobId=${jobId}`,
                headers: { Authorization: "Bearer " + jwt_token },
            });
        } catch (err) {
            console.log('Error assigning job:', err);
        }
    },

    completeMyJob: async ({ request, locals }) => {
        const jwt_token = locals.jwt_token;
        if (!jwt_token) {
            throw error(401, 'Authentication required');
        }
        const data = await request.formData();
        const jobId = data.get('jobId');
        try {
            await axios({
                method: "put",
                url: `${API_BASE_URL}/api/service/me/completejob?jobId=${jobId}`,
                headers: { Authorization: "Bearer " + jwt_token },
            });
        } catch (err) {
            console.log('Error completing job:', err);
        }
    }
}
