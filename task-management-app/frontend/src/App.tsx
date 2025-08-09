import './index.css'

function App() {
  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center">
      <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full mx-4">
        <h1 className="text-3xl font-bold text-blue-600 mb-4 text-center">
          Task Management App
        </h1>
        <p className="text-gray-600 text-center mb-6">
          Welcome to your personal task manager! 🎉
        </p>
        <div className="space-y-4">
          <button className="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg transition duration-200">
            View Tasks
          </button>
          <button className="w-full bg-green-500 hover:bg-green-700 text-white font-bold py-3 px-4 rounded-lg transition duration-200">
            Add New Task
          </button>
        </div>
      </div>
    </div>
  )
}

export default App
