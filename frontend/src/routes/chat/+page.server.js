import axios from 'axios';
import { error } from '@sveltejs/kit';
import 'dotenv/config';
const API_BASE_URL = process.env.API_BASE_URL;

export const actions = {
    sendMessage: async ({ request, locals }) => {
        const jwt_token = locals.jwt_token;
        if (!jwt_token) {
            throw error(401, 'Authentication required');
        }

        const data = await request.formData();
        const message = data.get('message');

        try {
            const response = await axios({
                method: 'post',
                url: `${API_BASE_URL}/api/chat`,
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: 'Bearer ' + jwt_token
                },
                data: { message }
            });
            return { success: true, reply: response.data.reply };
        } catch (e) {
            console.log('Chat error:', e.message);
            return { success: false, error: 'Chat-Anfrage fehlgeschlagen' };
        }
    }
};
