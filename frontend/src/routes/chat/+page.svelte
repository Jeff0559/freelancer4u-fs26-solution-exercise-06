<script>
    let messages = $state([]);
    let inputMessage = $state('');
    let loading = $state(false);

    async function sendMessage() {
        if (!inputMessage.trim()) return;

        const userMessage = inputMessage;
        messages = [...messages, { role: 'user', text: userMessage }];
        inputMessage = '';
        loading = true;

        try {
            const response = await fetch('?/sendMessage', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams({ message: userMessage })
            });
            const result = await response.json();
            const data = JSON.parse(result.data);
            const reply = data[1] || 'Keine Antwort erhalten.';
            messages = [...messages, { role: 'assistant', text: reply }];
        } catch (e) {
            messages = [...messages, { role: 'assistant', text: 'Fehler: ' + e.message }];
        } finally {
            loading = false;
        }
    }
</script>

<div class="container mt-4">
    <h1>💬 Chat mit dem Freelancer4U Assistenten</h1>

    <div class="card mt-3" style="height: 500px; overflow-y: auto;">
        <div class="card-body">
            {#each messages as msg}
                <div class="mb-2 {msg.role === 'user' ? 'text-end' : 'text-start'}">
                    <span class="badge {msg.role === 'user' ? 'bg-primary' : 'bg-secondary'}">
                        {msg.role === 'user' ? 'Du' : 'Assistent'}
                    </span>
                    <p class="mb-0">{msg.text}</p>
                </div>
            {/each}
            {#if loading}
                <p class="text-muted"><em>Assistent denkt nach...</em></p>
            {/if}
        </div>
    </div>

    <div class="input-group mt-3">
        <input
            type="text"
            class="form-control"
            placeholder="Schreibe eine Nachricht..."
            bind:value={inputMessage}
            onkeydown={(e) => e.key === 'Enter' && sendMessage()}
            disabled={loading}
        />
        <button class="btn btn-primary" onclick={sendMessage} disabled={loading}>
            Senden
        </button>
    </div>
</div>
