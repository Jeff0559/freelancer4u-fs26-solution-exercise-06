import axios from "axios";
import { error } from "@sveltejs/kit";
// Load environment variables from .env file for local development
import 'dotenv/config';
const API_BASE_URL = process.env.API_BASE_URL; // defined in frontend/.env

export async function load({ url, locals }) {
    const jwt_token = locals.jwt_token;

    if (!jwt_token) {
        return {
            companies: [],
            nrOfPages: 0,
            currentPage: 1
        };
    }

    try {
        const currentPage = parseInt(url.searchParams.get('pageNumber') || '1');
        const pageSize = parseInt(url.searchParams.get('pageSize') || '4');

        const query = `?pageSize=${pageSize}&pageNumber=${currentPage}`;

        const response = await axios({
            method: "get",
            url: `${API_BASE_URL}/api/company` + query,
            headers: { Authorization: "Bearer " + jwt_token },
        });

        return {
            companies: response.data.content,
            nrOfPages: response.data.totalPages || 0,
            currentPage: currentPage
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
            const disifyResponse = await axios.get(`https://disify.com/api/email/${company.email}`);
            const mailInfo = disifyResponse.data;
            console.log(`Disify validation for ${company.email}:`, mailInfo);
            if (!mailInfo.format || mailInfo.disposable || !mailInfo.dns) {
                return { success: false, error: `Email ${company.email} is not valid.` };
            }
        } catch (err) {
            console.log('Disify API error:', err);
            return { success: false, error: `Email ${company.email} is not valid.` };
        }

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
        } catch (err) {
            console.log('Error creating company:', err);
            return { success: false, error: 'Could not create company' };
        }
    }
};
