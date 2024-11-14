# -
使用Gossiping协议实现去中心化的平均数算法，使用多线程模拟多节点代码的方式

# Project Overview
This project implements a decentralized averaging algorithm based on the Gossip protocol using Java. The algorithm simulates multiple nodes in a multi-threaded environment, where each node shares its state and information with others in an effort to converge towards a global average. The simulation involves multiple instances of GossipNode, with each node updating its value based on interaction with other nodes, aiming for convergence and reduced error over several iterations.
# Functionality
The project consists of three main components: the GossipNode class, which represents individual nodes and manages their state and value updates; GossipSimulation1 and GossipSimulation2, which simulate different configurations and study the effect of parameters like node count and the gossip range (K value) on convergence performance; and a Python-based tool for visualizing the results. Key functionalities include node value propagation, convergence detection, error calculation, and multi-threaded synchronization using mechanisms like ExecutorService and CountDownLatch.
# Key Features
The project features a decentralized structure where nodes interact with each other without a central controller. It also employs multi-threading for concurrent execution, ensuring that node interactions happen in parallel while maintaining synchronization. The experiment allows users to explore how different parameters, such as node count and gossip range, affect convergence speed and error. Additionally, the results are visualized using Python, providing clear insights into how these variables influence the algorithm's performance.
