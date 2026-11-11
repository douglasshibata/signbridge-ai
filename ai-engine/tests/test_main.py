from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

def test_health_check():
    response = client.get("/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "ok"
    assert data["engine"] == "pytorch"
    assert "device" in data

def test_predict():
    # Input size is 1662 as defined in model.py
    landmarks = [0.0] * 1662
    response = client.post("/predict", json=[{"landmarks": landmarks}])
    assert response.status_code == 200
    assert "sign" in response.json()
    assert "confidence" in response.json()
