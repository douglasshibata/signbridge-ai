from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import numpy as np
import torch
from model import get_model

app = FastAPI(title="SignBridge AI Engine")

# Initialize model
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model = get_model(num_classes=10).to(device)
model.eval()

class Keypoints(BaseModel):
    # Simplified representation: a list of floats representing normalized landmarks
    landmarks: List[float]

class PredictionResponse(BaseModel):
    sign: str
    confidence: float

# Mock labels
LABELS = ["hello", "thank you", "yes", "no", "goodbye", "please", "sorry", "help", "school", "home"]

@app.get("/health")
async def health_check():
    return {
        "status": "ok",
        "engine": "pytorch",
        "device": str(device)
    }

@app.post("/predict", response_model=PredictionResponse)
async def predict(data: List[Keypoints]):
    if not data:
        raise HTTPException(status_code=400, detail="No data provided")

    try:
        # Convert input data to tensor
        # data is List[Keypoints], each Keypoints has .landmarks (List[float])
        input_data = [item.landmarks for item in data]
        input_tensor = torch.tensor([input_data], dtype=torch.float32).to(device)

        with torch.no_grad():
            output = model(input_tensor)
            probabilities = torch.softmax(output, dim=1)
            confidence, predicted_idx = torch.max(probabilities, dim=1)

        return {
            "sign": LABELS[predicted_idx.item()],
            "confidence": confidence.item()
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
