const video = document.getElementById('webcam');
const startBtn = document.getElementById('start-btn');
const output = document.getElementById('output');
const status = document.getElementById('status');

let ws;

async function setupWebcam() {
    try {
        const stream = await navigator.mediaDevices.getUserMedia({ video: true });
        video.srcObject = stream;
    } catch (err) {
        console.error("Error accessing webcam:", err);
        output.innerText = "Error: Webcam not accessible";
    }
}

function connectWebSocket() {
    ws = new WebSocket('ws://localhost:8080/ws/translate');

    ws.onopen = () => {
        status.innerText = "Status: Connected";
        status.className = "mt-4 text-sm text-green-500";
    };

    ws.onmessage = (event) => {
        output.innerText = event.data;
    };

    ws.onclose = () => {
        status.innerText = "Status: Disconnected";
        status.className = "mt-4 text-sm text-red-500";
    };
}

startBtn.addEventListener('click', () => {
    if (!ws || ws.readyState !== WebSocket.OPEN) {
        connectWebSocket();
    }
    // Simple mock: send a ping every 2 seconds
    setInterval(() => {
        if (ws && ws.readyState === WebSocket.OPEN) {
            ws.send("ping");
        }
    }, 2000);
});

setupWebcam();
