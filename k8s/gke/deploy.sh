#!/bin/bash
kubectl apply -f chat-deployment.yaml
kubectl apply -f chat-service.yaml
kubectl apply -f postgres-configmap.yaml
kubectl apply -f postgres-secret.yaml
kubectl apply -f postgres-deployment.yaml
kubectl apply -f postgres-service.yaml