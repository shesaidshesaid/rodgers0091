// src/App.js

import React from 'react';
import { Layout, Typography } from 'antd';
import AddTarefa from './components/AddTarefa';
import TarefasList from './components/TarefasList';

const { Header, Content } = Layout;
const { Title } = Typography;

function App() {
  return (
    <Layout>
      <Header>
        <Title style={{ color: 'white', margin: '14px 0' }}>Gerenciador de Tarefas</Title>
      </Header>
      <Content style={{ padding: '20px' }}>
        <AddTarefa />
        <TarefasList />
      </Content>
    </Layout>
  );
}

export default App;
