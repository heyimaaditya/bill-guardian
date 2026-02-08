import { useEffect } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export const useWebSocket = (onMessageReceived: () => void) => {
  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws-cfo');
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        console.log('Connected to BillGuardian WebSocket');
        client.subscribe('/topic/updates', (message) => {
          if (message.body === 'AUDIT_COMPLETE') {
            onMessageReceived(); // Refresh the data!
          }
        });
      },
      onStompError: (frame) => {
        console.error('WebSocket Error: ' + frame.headers['message']);
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, [onMessageReceived]);
};